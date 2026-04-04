package main;

import javax.swing.SwingUtilities;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import auth.LoginPage;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File dataFolder = new File("data");
        if (!dataFolder.exists()) {
            boolean created = dataFolder.mkdir();
            if (created) {
                System.out.println("The 'data' folder was created successfully.");
            } else {
                System.out.println("Failed to create the 'data' folder.");
            }
        } else {
            System.out.println("The 'data' folder already exists.");
        }

        // 2. Create the "account.txt" file with default admin if it doesn't exist
        File accountFile = new File(dataFolder, "account.txt");
        if (!accountFile.exists()) {
            try {
                boolean fileCreated = accountFile.createNewFile();
                if (fileCreated) {
                    FileWriter writer = new FileWriter(accountFile);
                    writer.write("ADM0001, AdminAPU, Admin123, Admin, 012-3456789, APUAdmin@gmail.com, \n");
                    writer.close();
                    System.out.println("The 'account.txt' file was created with default admin account.");
                }
            } catch (IOException e) {
                System.out.println("An error occurred creating account.txt.");
                e.printStackTrace();
            }
        }

        // 3. Launch the LoginPage as the first screen
        SwingUtilities.invokeLater(() -> {
            new LoginPage();
        });
	}

}
