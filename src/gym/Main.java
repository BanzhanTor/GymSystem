package gym;

import gym.ui.LoginMenu;
import gym.ui.MainMenu;

import java.util.Scanner;

/**
 * 健身房管理系统 程序入口。
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!LoginMenu.login(scanner)) {
            System.out.println("用户名或密码错误，退出系统。");
            return;
        }
        System.out.println("登录成功！");
        new MainMenu(scanner).run();
    }
}