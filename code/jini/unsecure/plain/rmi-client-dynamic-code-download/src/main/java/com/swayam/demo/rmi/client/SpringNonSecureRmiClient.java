package com.swayam.demo.rmi.client;

import java.awt.EventQueue;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.server.RMIClassLoaderSpi;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.swayam.demo.rmi.dto.BankDetail;

public class SpringNonSecureRmiClient {

    public static void main(String[] args) throws Exception {

        System.setProperty("java.security.policy", System.getProperty("user.home") + "/jini/policy.all");

        System.out.println("SpringNonSecureRmiClient.main() java.security.policy=" + System.getProperty("java.security.policy"));

        // the below line is put only for debugging purposes, its not needed, as
        // the default class loader is good enough
        System.setProperty(RMIClassLoaderSpi.class.getName(), MyClassProvider.class.getName());

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
            System.out.println("DynamicDownloadRmiClient.main() setting custom security manager");
        }

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("client-application.xml");
        Remote bankDetailService = (Remote) context.getBean("bankDetailService");
        Method getBankDetailsForJob = bankDetailService.getClass().getDeclaredMethod("getBankDetailsForJob");
        Map<String, List<BankDetail>> groupedBankDetails = (Map<String, List<BankDetail>>) getBankDetailsForJob.invoke(bankDetailService);
        showFrame(groupedBankDetails);

    }

    private static void showFrame(Map<String, List<BankDetail>> groupedBankDetails) {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

        EventQueue.invokeLater(() -> {
            new GroupingDemoFrame(groupedBankDetails).setVisible(true);
        });
    }

}