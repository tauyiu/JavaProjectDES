package com.company;


import java.io.*;
import java.util.Scanner;

public class Main2
{
    public static void main(String args[]) throws IOException
    {
        Question2 q2 = new Question2();
        String text = "";
        String key = "-1"; //initialise only, value will be changed, hexadecimal value for 0 is 30, if NULL then 00



        /*
        System.out.println("Input text");
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();    //String text = "P5LEIR*$";

        */

        System.out.println("Input key of any length (preferably 8)");
        Scanner sc2 = new Scanner(System.in);
        key = sc2.nextLine();    //String key = ")(*&^%$#";
        if (key == "")          //type 'nothing' to test all-zero key
        {
            key = null;
        }

        else if(key.toCharArray().length > 8)       //length of key > 8     CHOPPING
        {
            char[] charNum = key.toCharArray();
            if(charNum.length !=8)
            {
                System.out.println("Key before chopping= "+ key);
                while (charNum.length != 8)  //need 64 bits
                {
                    if (charNum.length > 8)
                    {
                        key = key.substring(0, key.length() - 1);
                        charNum = key.toCharArray();
                    }
                }
                System.out.println("Key after chopping = " + key + "\n");
            }
        }

        key = q2.stringToHex(key);
        if(key.toCharArray().length != 16)      //PADDING, need to have 8 hex value
        {
            System.out.println("Key(hex) before padding ="+ key);
            while(key.toCharArray().length!= 16)
            {
                key+= '0';   // add 0 to hex value at the back
            }
        }
        System.out.println("Key(hex) after padding  ="+ key);


        //Encrypt Process
        FileReader in = null;
        FileWriter out = null;
        try
        {
            out = new FileWriter("q2_output.txt");
            in = new FileReader("DES-test.txt");
            int c;
            int counter = 0;
            String character = "";

            while ((c = in.read()) != -1)
            {
                /*replace everything in this loop to see how "reading" character and its hex value
                char outerChar = (char) c;
                character += outerChar;
                out.write(counter + " " +cipher.stringToHex(character) +"\n");
                counter++;
                character = "";*/
                char outChar = (char) c;
                character += outChar;
                character = q2.stringToHex(character);
                while(character.length()<2)     //padding for hex with no 0 in front
                {
                    character = "0" + character;
                }
                if(text.toCharArray().length!=16)       //16 hex values, 1 hex = 4 bits, 16x4 = 64 bits
                {
                    text += character;
                    counter++;
                    //tesing-purpose System.out.println(text+" "+ counter);
                }
                if(text.toCharArray().length==16)
                {
                    text = q2.encryptionDES(text, key);   //already in hex
                    out.write(text.toUpperCase());
                    text = "";  //reset text value
                    counter = 0; //reset counter value
                }

                character = "";
            }
            if(text.toCharArray().length!=16 && counter != 0)
            {
                while(text.length()<16)
                {
                    text = text+"0";
                }
                //testing-purpose System.out.println(text);
                text = q2.encryptionDES(text,key);
                out.write(text.toUpperCase());
            }
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (out != null)
            {
                out.close();
            }
        }

        text = "";
        //Decrypt Process
        try
        {
            out = new FileWriter("q2_output_decryption.txt");
            in = new FileReader("q2_output.txt");

            int c;
            int counter = 0;
            String character = "";

            while ((c = in.read()) != -1)
            {
                char outChar = (char) c;
                character += outChar;           //character is already in hex value

                if(text.toCharArray().length!=16)
                {
                    text += character;
                    counter++;
                    //testing-purpose System.out.println(text+" "+ counter);
                }
                if(text.toCharArray().length==16)
                {
                    text = q2.decryptionDES(text,key);   //already in hex
                    out.write(q2.hexToString(text));
                    text ="";  //reset text value
                    counter =0; //reset counter value
                }
                character="";
            }
            if(text.toCharArray().length!=16 && counter !=0)
            {
                while(text.length()<16)
                {
                    text = text+ "0";
                }
                //testing-purpose System.out.println(text);
                text = q2.decryptionDES(text,key);
                out.write(q2.hexToString(text));
            }

        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (out != null)
            {
                out.close();
            }
        }
        System.out.println("Encrypted text is in q2_output.txt, Decrypted text is in q2_output_decryption.txt");

        /* to test all-zeroes key
        System.out.println("Encryption:\n");
        text = cipher.encrypt(text, key);
        System.out.println("\nCipher Text in hex: " + text.toUpperCase() + "\n");
        System.out.println("\nCipher Text in string: " + test.hexToString(text));
        */



    }
}






