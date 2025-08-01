package com.khalil.pdfviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.khalil.pdfviewer.Database.FileUtils;

import java.io.InputStream;

public class PdfViewActivity extends AppCompatActivity {

    private PDFView pdfView;
    private TextView pageIndicator, fileNameText, fileSizeText, filePathText;
    private ImageButton zoomInBtn, zoomOutBtn, infoBtn, shareBtn,backBtn;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    private int currentPage = 0;
    private int totalPages = 0;
    private float currentZoom = 1.0f;

    private Uri pdfUri;
    private String fileName, filePath;
    private long fileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        initViews();


        Intent intent = getIntent();
        pdfUri = intent.getData();

        if (pdfUri != null) {
            fileName = FileUtils.getFileName(this, pdfUri);
            fileSize = FileUtils.getFileSize(this, pdfUri);
            filePath = FileUtils.getFilePath(this, pdfUri);

            loadPdf(pdfUri);
            populateFileInfo();
        } else {
            Toast.makeText(this, "PDF file not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupListeners();
    }

    private void initViews() {
        pdfView = findViewById(R.id.pdfView);
        pageIndicator = findViewById(R.id.pageIndicator);
        zoomInBtn = findViewById(R.id.zoomIn);
        zoomOutBtn = findViewById(R.id.zoomOut);
        infoBtn = findViewById(R.id.infoButton);
        shareBtn = findViewById(R.id.shareButton);
        backBtn = findViewById(R.id.backButton);

        fileNameText = findViewById(R.id.fileNameText);
        fileSizeText = findViewById(R.id.fileSizeText);
        filePathText = findViewById(R.id.filePathText);

        View bottomSheet = findViewById(R.id.bottomSheetLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PdfViewActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();

            }
        });
    }

    private void loadPdf(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);

            pdfView.fromStream(inputStream)
                    .defaultPage(currentPage)
                    .enableSwipe(true)               // Vertical swipe
                    .swipeHorizontal(false)          // Vertical scrolling
                    .enableDoubletap(true)           // Double-tap to zoom
                    .enableAntialiasing(true)        // Better rendering
                    .spacing(4)                      // Space between pages
                    .pageFling(false)                // Disable page fling for smooth scroll
                    .pageSnap(false)                 // Disable snapping to page
                    .scrollHandle(new DefaultScrollHandle(this)) // Adds scroll thumb
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            currentPage = page;
                            totalPages = pageCount;
                            updatePageIndicator();
                        }
                    })
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            totalPages = nbPages;
                            updatePageIndicator();
                        }
                    })
                    .load();

        } catch (Exception e) {
            Toast.makeText(this, "Error loading PDF", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        zoomInBtn.setOnClickListener(v -> {
            currentZoom += 0.2f;
            pdfView.zoomTo(currentZoom);
            pdfView.invalidate();
        });

        zoomOutBtn.setOnClickListener(v -> {
            currentZoom = Math.max(1.0f, currentZoom - 0.2f);
            pdfView.zoomTo(currentZoom);
            pdfView.invalidate();
        });

        infoBtn.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        shareBtn.setOnClickListener(v -> {
            if (pdfUri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("application/pdf");
                shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                startActivity(Intent.createChooser(shareIntent, "Share PDF"));
            }
        });
    }

    private void updatePageIndicator() {
        pageIndicator.setText((currentPage + 1) + " / " + totalPages);
    }

    private void populateFileInfo() {
        fileNameText.setText("File Name: " + fileName);
        fileSizeText.setText("Size: " + FileUtils.formatFileSize(fileSize));
        filePathText.setText("Location: " + filePath);
    }
}
