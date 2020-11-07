import Exceptions.FileDoesntThisByteCode;
import Exceptions.FileWithThisExtensionDoesntExist;
import Exceptions.SameByteCode;
import Exceptions.WrongFormatOfByteCode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Stworzyć (przy użyciu ulubionego programu do pisania kodu) program do użytkowania przez szeroką rzeszę użytkowników
 * do wprowadzania danych (ciągów) i odczytywania plików z folderu, wyszukiwania w nim i podfolderach plików o konkretnym
 * rozszerzeniu, wyciąganiu jego kodu bitoweg(bajtowego?) i zapisywania pod tą samą nazwą jako inny kod bitowy.
 * <p>
 * Architektura:
 * - wyszukiwanie - ścieżka do folderu i podfolderów
 * - odczyt - sprawdzanie czy plik istnieje i czy ma (wyszukiwane rozszerzenie) np. .jpeg ; czytanie kodu bitowego
 * - zapis - zapisanie pliku jako nowy
 * <p>
 * obsługa błędów
 */

public class QBSTask extends JFrame {
    private JPanel mainPanel;
    private JButton sendButton;
    private JTextField givenPathToCatalog;
    private JTextField givenExtensionOfFile;
    private JTextField givenByteCodeToChange;
    private JTextField givenByteCodeToExchange;
    private JLabel givePathToCatalog;
    private JLabel giveExtensionOfFile;
    private JLabel giveByteStringToExchange;
    private JLabel giveByteStringToChange;
    private JTextArea result;

    public QBSTask(String title) {
        super("QBS Task");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (givenPathToCatalog.getText().isEmpty() ||
                        givenExtensionOfFile.getText().isEmpty() ||
                        givenByteCodeToChange.getText().isEmpty() ||
                        givenByteCodeToExchange.getText().isEmpty()) {
                    result.setText("Jedno lub więcej z pól jest pustych, proszę wprowadzić poprawne dane");
                    return;
                }

                Service service = new Service();
                String path = givenPathToCatalog.getText();
                String extension = givenExtensionOfFile.getText();
                String bytesToRemove = givenByteCodeToChange.getText();
                String bytesToAdd = givenByteCodeToExchange.getText();

                String message = null;
                try {
                    message = service.byteChecker(path, extension, bytesToRemove, bytesToAdd);
                } catch (WrongFormatOfByteCode wrongFormatOfByteCode) {
                    message = wrongFormatOfByteCode.getMessage();
                } catch (SameByteCode sameByteCode) {
                    message = sameByteCode.getMessage();
                } catch (FileDoesntThisByteCode fileDoesntThisByteCode) {
                    message = fileDoesntThisByteCode.getMessage();
                } catch (FileWithThisExtensionDoesntExist fileWithThisExtensionDoesntExist) {
                    message = fileWithThisExtensionDoesntExist.getMessage();
                }
                result.setText(message);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new QBSTask("QBS Task");
        frame.setVisible(true);

    }

}
