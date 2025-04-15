package com.handong.oh318;

import com.handong.oh318.core.javaimpl.JavaSourceCodeExtractor;

public class Main {

    // public static void main(String[] args) {
    // String javaPath = args[0] ;
    // String drawioPath = args[1] ;

    // System.out.println("Java Path: " + javaPath);
    // System.out.println("DrawioPath: " + drawioPath);

    // // Extractor
    // if ( args[2].equals("0") ) {
    // // "/Users/jinil/Desktop/create"
    // // "/Users/jinil/Desktop/test.drawio"
    // Extractor extractor = new Extractor(javaPath, drawioPath);

    // extractor.createDrawio();

    // // Coder
    // } else if ( args[2].equals("1")) {
    // System.out.println("[Coder]") ;
    // Coder coder = new Coder() ;

    // boolean success = coder.createSourceCodes(javaPath, drawioPath) ;

    // if ( success ) {
    // System.out.println("Create Success") ;
    // } else {
    // System.out.println("Cannot create java source codes") ;
    // }
    // }
    // }

    public static void main(String[] args) {
        String javaPath = "/home/user/gh/jdiagram/converter/src/test/resource/create";
        String drawioPath = "./jd.drawio";

        System.out.println("Java Path: " + javaPath);
        System.out.println("DrawioPath: " + drawioPath);
        var extractor = new JavaSourceCodeExtractor(javaPath);
        var javaSource = extractor.extractSource();
        javaSource.forEach(System.out::println);

        // Extractor
        // if (args[2].equals("0")) {
        // // "/Users/jinil/Desktop/create"
        // // "/Users/jinil/Desktop/test.drawio"
        // Extractor extractor = new Extractor(javaPath, drawioPath);

        // extractor.createDrawio();

        // // Coder
        // } else if (args[2].equals("1")) {
        // System.out.println("[Coder]");
        // Coder coder = new Coder();

        // boolean success = coder.createSourceCodes(javaPath, drawioPath);

        // if (success) {
        // System.out.println("Create Success");
        // } else {
        // System.out.println("Cannot create java source codes");
        // }
        // }
    }
}
