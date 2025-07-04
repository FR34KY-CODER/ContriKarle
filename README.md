# 📟 Contrikarle - Smart Expense Splitter App

![Jetpack Compose](https://img.shields.io/badge/Made%20With-Jetpack%20Compose-4285F4?logo=android\&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-purple?logo=kotlin\&logoColor=white)
![Firebase](https://img.shields.io/badge/Backend-Firebase-orange?logo=firebase)

**Contrikarle** is a stylish and intuitive expense-splitting app built in **Kotlin with Jetpack Compose**, aimed to make shared finances easy and efficient. It's designed to handle group expenses, events, smart settlements, and a slick UI better than traditional apps like Splitwise.

---

## 🚀 Features

* 🔐 **Google Sign-In** (Firebase Auth)
* 🏠 **Dashboard** to manage all group/event expenses
* 👥 **Group & Friend Management**
* 💸 **Smart Expense Split**

  * Equal, Percentage, or Custom splitting
* 🧠 **Auto-Settle Logic**

  * Calculate who owes whom the least
* 📊 **Detailed History & Graphs**
* 🔔 **Notifications & Activity Logs**
* 🧠 (Upcoming) **Paytm SMS Scanner & OCR-Based Entry**
* 💅 Fully responsive, beautiful **Jetpack Compose UI**

---

## 📱 Tech Stack

<table>
  <thead>
    <tr>
      <th>Layer</th>
      <th>Tools/Tech</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Language</td>
      <td>Kotlin</td>
    </tr>
    <tr>
      <td>UI Framework</td>
      <td>Jetpack Compose</td>
    </tr>
    <tr>
      <td>State Mgmt</td>
      <td>ViewModel + StateFlow</td>
    </tr>
    <tr>
      <td>Auth</td>
      <td>Firebase Auth (Google Sign-In)</td>
    </tr>
    <tr>
      <td>Backend DB</td>
      <td>Firebase Firestore</td>
    </tr>
    <tr>
      <td>Navigation</td>
      <td>Jetpack Navigation Compose</td>
    </tr>
    <tr>
      <td>DI</td>
      <td>(Coming Soon) Hilt</td>
    </tr>
    <tr>
      <td>Testing</td>
      <td>(Coming Soon) JUnit + Compose Testing</td>
    </tr>
  </tbody>
</table>

---

## 🏧 App Structure

```
com.example.contrikarle
│
├── presentation/
│   ├── screens/         # All UI screens (Login, Home, Group, etc.)
│   ├── auth/            # GoogleAuthUiClient & Auth logic
│   └── components/      # Reusable UI Composables
│
├── viewmodel/           # ViewModels for each screen
├── model/               # Data classes (User, Expense, Group, etc.)
├── repository/          # Firebase access layer (coming soon)
└── utils/               # Helper functions, constants, etc.
```

---

## 🔧 Setup Instructions

> ⚠️ Make sure you have [Android Studio Hedgehog+](https://developer.android.com/studio) installed and a working Firebase project.

1. Clone this repo:

   ```bash
   git clone https://github.com/FR34KY-CODER/Contrikarle.git
   ```
   Also rename the package name for the project

2. Add your `google-services.json` file in:

   ```
   app/google-services.json
   ```

3. Paste your **Web Client ID** from Firebase into:

   * `GoogleAuthUiClient.kt`
   * `strings.xml` (optional, for dynamic injection)

4. Build & Run the app:

   * Connect a device or emulator
   * Hit `Run ▶` in Android Studio

---

## 🌄 Screenshots

> Coming Soon — once UI is fully built.

---

## ✨ TODO (Milestones)

* [x] Firebase Auth with Google Sign-In
* [ ] Home Screen with Group/Event Listing
* [ ] Group Creation with Member Add
* [ ] Expense Creation with Dynamic Splits
* [ ] Firebase Firestore Integration
* [ ] Settle Up Smart Logic
* [ ] Notifications for Due Expenses
* [ ] SMS & OCR-based Expense Extraction
* [ ] Polished UI + Animations
* [ ] Deploy to Play Store

---

## 🧠 About the Developer

Made with 💙 by [FR34K (Ojasvi Goyal)](https://github.com/FR34KY-CODER)
Game dev, ML dev, Kotlin madlad, and hardcore caffeine-fueled builder.
**This app is part of a long journey to build polished, useful apps — fast and solo.**

---

## 📄 License

This project is licensed under the [Apache License](LICENSE).

---

> ⚡ *Contrikarle* — "Contri Karo, Dost bane Raho! :D."
