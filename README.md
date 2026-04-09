## 🛠️ How to Clone & Setup (Eclipse)

Follow these steps exactly to ensure your local environment matches the repository:

1. **Copy the Clone URL** from GitHub.
2. In Eclipse, go to **File > Import...**
3. Select **Git > Projects from Git (with smart import)** and click **Next**.
4. Select **Clone URI** and click **Next**.
5. Paste the URI, ensure your credentials are correct, and click **Next** through the branch selection (main).
6. **CRITICAL:** On the "Wizard for project import" screen, ensure the project is recognized as a **Java Project**.

   * *Do NOT select "Convert to Maven" if prompted.*
7. Click **Finish**.

---

## ⚠️ Important Rules (Read Before Coding)

* **NO MAVEN:** Do not right-click the project and select `Configure > Convert to Maven Project`. We are using standard Java libraries only.
* **NO FORCE PUSH:** Never use `git push -f`. If you get an error, ask the team for help.
* **Package Structure:**

  * `main`: Entry point (`Main.java`)
  * `auth`: Login, Signup, and Reset Password screens (all related files go here)

---

## 🚨 Source Code Issue

If you encounter unexpected errors or missing files:

* Delete the repository locally
* Re-clone and import again following the steps above

---

## 🛠️ Git Workflow Guide (MAIN BRANCH ONLY)

To simplify collaboration, we are working **directly on the `main` branch**.

⚠️ This requires discipline — always pull before you start and before you push.

---

## 📥 START: Always Pull First

Before opening Eclipse or modifying any code:

```bash
git pull origin main
```

---

## 💻 WORK: Make Your Changes

* Open the project in Eclipse
* Make your changes
* Ensure your code compiles and runs correctly

---

## 🚀 FINISH: Safe Commit & Push

### 1. Stage and commit your changes

```bash
git add .
git commit -m "Clear description of what you did"
```

---

### 2. Pull again to avoid overwriting others' work

```bash
git pull origin main
```

---

### 3. Push directly to main

```bash
git push origin main
```

---

## ⚠️ Conflict Handling

If you encounter merge conflicts during `git pull`:

* Do NOT panic
* Do NOT force push
* Resolve conflicts carefully or ask the team for help

---

## ✅ Best Practices

* Pull frequently (before and after coding)
* Commit small, meaningful changes
* Write clear commit messages
* Communicate with teammates before making major changes

---

## ❌ What NOT to Do

* Do NOT create branches
* Do NOT use `git push -f`
* Do NOT overwrite other people's work

---

Following this workflow ensures everyone stays in sync while working directly on the `main` branch.
