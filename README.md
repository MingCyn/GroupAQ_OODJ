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

To prevent losing your work, follow these three steps every time you sit down to code:
### START: Always Pull First 📥
Before you open Eclipse or touch any code, get the latest updates from the team:
```bash
git pull origin main

### **WORK: Use Your Own Branch **
# Create and switch to your personal branch
git checkout -b yourname-featurename

### **FINISH: The "Safe" Push 🚀**
# 1. Save your changes
git add .
git commit -m "Clear description of what you did"

# 2. IMPORTANT: Pull one last time to check for conflicts
git pull origin main

# 3. Push your branch to GitHub
git push origin yourname-featurename

Once your branch is pushed to GitHub:
Go to the GitHub website.
Click "Compare & pull request" for your branch.
