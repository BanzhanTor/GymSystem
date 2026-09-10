package gym.ui;

import java.util.Scanner;

/**
 * 登录界面：简单的用户名/密码校验。
 */
public class LoginMenu {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";

    public static boolean login(Scanner sc) {
        System.out.println("========== 健身房管理系统 登录 ==========");
        System.out.print("用户名：");
        String user = sc.nextLine().trim();
        System.out.print("密码：");
        String pwd = sc.nextLine().trim();
        return USERNAME.equals(user) && PASSWORD.equals(pwd);
    }
}