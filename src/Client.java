import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 9799);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your nickname : ");
            output.println(scanner.nextLine());
            String message;
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while ((message = scanner.nextLine()) != "") {
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                System.out.println(bufferedReader.readLine());
            }
            String string = "";
            while (!string.equals("quit")) {
                string = scanner.nextLine();
                output.println(string);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}