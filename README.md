## 🛠️ How to Clone & Setup (Eclipse)

Follow these steps exactly to ensure your local environment matches the repository:

1. **Copy the Clone URL** from GitHub.
2. In Eclipse, go to **File > Import...**
3. Select **Git > Projects from Git (with smart import)** and click **Next**.
4. Select **Clone URI** and click **Next**.
5. Paste the URI, ensure your credentials are correct, and click **Next** through the branch selection (master).
6. **CRITICAL:** On the "Wizard for project import" screen, ensure the project is recognized as a **Java Project**. 
   - *Do NOT select "Convert to Maven" if prompted.*
7. Click **Finish**.

## ⚠️ Important Rules (Read Before Coding)
- **NO MAVEN:** Do not right-click the project and select `Configure > Convert to Maven Project`. We are using standard Java libraries only.
- **NO OVERWRITING:** Never force a push (`git push -f`). If you get an error, ask the team for help.
- **Package Structure:** - `main`: Entry point (`Main.java`)
    - `auth`: Login, Signup, and Reset Password screens, all project should be here!

 ## **Source Code Issue**
 Please delete the repo and import again!

# 🛠️ Git Workflow Guide (Team Standard)
To prevent losing your work and avoid conflicts, follow these steps **every time you start coding**:

---
## 📥 START: Always Pull First
Before opening Eclipse or modifying any code, pull the latest changes:
```bash
git pull origin main
```
---
## 🌿 WORK: Use Your Own Branch
Create and switch to a personal feature branch:
```bash
git checkout -b yourname-featurename
```
---
## 🚀 FINISH: The "Safe" Push
### 1. Save your changes
```bash
git add .
git commit -m "Clear description of what you did"
```
---
### 2. Pull again to avoid conflicts
```bash
git pull origin main
```
---

### 3. Push your branch to GitHub
```bash
git push origin yourname-featurename
```
---

## 🔁 Create a Pull Request
Once your branch is pushed:
1. Go to your repository on GitHub  
2. Click **"Compare & pull request"**  
3. Add a clear description of your changes  
4. Submit the pull request for review  
---
