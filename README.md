<h1 align="center">📄 Android PDF Viewer App</h1>

<p align="center">
  <b>ARCH TECHNOLOGIES Internship Task</b><br>
  A full-featured, elegant, and modern PDF Viewer app built using <b>Java</b> and <b>XML</b> in Android Studio.
</p>

---

## 📝 Task Overview
---

This project was developed as part of the **Android App Development Module** under the **ARCH TECHNOLOGIES** internship program.  
The objective was to create a robust, user-friendly PDF viewing application with modern UI and essential file management features.

---

## 🚀 Features
---

- 📂 Pick and open PDF files from device storage
- 🕘 Automatically track **recently opened files**
- 🔍 Search through recent PDFs by name
- ↕️ Sort recent files by **name**, **newest**, or **oldest**
- 📤 Share PDF files with other apps
- 🔎 Zoom controls and swipe between pages
- 📊 Bottom sheet with file details (name, size, location)
- 🧹 Delete individual or all recent files
- 🌙 Toggle **Night Mode** for easier viewing
- ⚡ Smooth transitions with clean and responsive UI

---

## 📱 Screenshots
---

| 🏠 Splash Screen | 📄 Home Screen | 📋 File Info Sheet |
|:--------------:|:-------------:|:------------------:|
| <img width="301" height="535" alt="Home Screen" src="https://github.com/user-attachments/assets/7ae4555b-1be3-43bf-9148-aca1e4f1055d" /> | <img width="301" height="526" alt="PDF View" src="https://github.com/user-attachments/assets/a189f8a7-ce56-4aff-9381-3f966c46b966" /> | <img width="260" height="568" alt="File Info" src="https://github.com/user-attachments/assets/ab9045a1-5421-4794-9072-14204d65c525" /> |

---

## 🔧 Technologies Used
---

- **Java** – Core business logic and event handling
- **XML** – UI layouts using ConstraintLayout, CardView, etc.
- **[AndroidPdfViewer](https://github.com/mhiew/android-pdf-viewer)** – PDF rendering engine
- **SharedPreferences** – Store and manage recent file history
- **BottomSheetDialog** – Display file info in a modal view
- **Material Components** – For modern dark/light UI styling

---

## 📦 Dependency
---

To render PDFs, add the following dependency to your `build.gradle (app)`:

```groovy
implementation 'com.github.mhiew:android-pdf-viewer:3.2.0-beta.3'
```
And make sure to enable mavenCentral() in your repositories:

```
repositories {
    google()
    mavenCentral()
}
```
📁 Project Structure
---
```
com.khalil.pdfviewer
├── MainActivity.java
├── PdfViewActivity.java
│
├── adapter/
│   └── RecentFilesAdapter.java
│
├── dashboard/
│   ├── FileUtils.java
│   └── RecentFileManager.java
│
├── model_class/
│   └── RecentFile.java
│
└── res/
    ├── layout/
    ├── drawable/
    └── values/
  ```
🧑‍💻 Author
---
- Muhammad Khalil Akbar Khan
- Android Developer | BBIT Student – UET Lahore
- 📧 mkhalilakbarkhan@gmail.com
- 🔗 GitHub | LinkedIn

🏷 License
---
This project is for educational and internship submission purposes under ARCH TECHNOLOGIES.
PDF rendering powered by the AndroidPdfViewer open-source library.
