import Exceptions.FileDoesntThisByteCode;
import Exceptions.FileWithThisExtensionDoesntExist;
import Exceptions.SameByteCode;
import Exceptions.WrongFormatOfByteCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Service {
    public String byteChecker(String path, String extension, String byteToRemove, String byteToAdd)
            throws WrongFormatOfByteCode, SameByteCode, FileDoesntThisByteCode, FileWithThisExtensionDoesntExist {
        if (!ifStringIsABytecodeChecker(byteToRemove) || !ifStringIsABytecodeChecker(byteToAdd)) {
            throw new WrongFormatOfByteCode("Nieprawidłowy format kodu ByteCode");
        }
        if (byteToRemove.equals(byteToAdd)) {
            throw new SameByteCode("ByteCode nie może być taki sam");
        }
        List<Byte> bytesToRemove = stringToByteListConverter(byteToRemove);
        List<Byte> bytesToAdd = stringToByteListConverter(byteToAdd);
        return fileByteChanger(path, extension, bytesToRemove, bytesToAdd);
    }

    private String fileByteChanger(String path, String extension, List<Byte> bytesToRemove, List<Byte> bytesToAdd)
            throws FileWithThisExtensionDoesntExist, FileDoesntThisByteCode {
        boolean fileWasChanged = false;
        int counter = 0;
        List<File> clearList = new ArrayList<>();
        List<File> files = ifMatchingFileExist(path, extension, clearList);

        if (files.size() == 0) {
            throw new FileWithThisExtensionDoesntExist("Nie istnieje plik, który ma rozszerzenie " + extension + "" +
                    " w katalogu " + "'" + path + "'");
        } else {
            for (File file : files) {
                boolean result = singleFileByteChanger(file.getAbsolutePath(), bytesToRemove, bytesToAdd);
                if (result) {
                    fileWasChanged = true;
                    counter++;
                }
            }
        }
        if (!fileWasChanged) {
            throw new FileDoesntThisByteCode("Żaden plik nie zawiera podanego ciągu bajtów");
        }
        return message(counter);
    }

    private boolean singleFileByteChanger(String absolutePath, List<Byte> bytesToRemove, List<Byte> bytesToAdd) {
        boolean flag = false;

        try {
            byte[] bytes = Files.readAllBytes(Paths.get(absolutePath));
            List<Byte> byteContent = new ArrayList<>();
            for (Byte i : bytes) {
                byteContent.add(i);
            }
            while (Collections.indexOfSubList(byteContent, bytesToRemove) != -1) {
                flag = true;
                int start = Collections.indexOfSubList(byteContent, bytesToRemove);
                byteContent.subList(start, start + bytesToRemove.size()).clear();
                byteContent.addAll(start, bytesToAdd);
            }
            byte[] byteArray = new byte[byteContent.size()];
            for (int i = 0; i < byteContent.size(); i++) {
                byteArray[i] = byteContent.get(i);
            }
            File file = new File(absolutePath);
            OutputStream os = new FileOutputStream(file);
            os.write(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    private List<Byte> stringToByteListConverter(String stringToConvert) {
        stringToConvert = stringToConvert.replaceAll("\\s+", "");
        String[] strings = stringToConvert.split(",");
        List<Byte> bytes = new ArrayList<>();
        for (String i : strings) {
            bytes.add(Byte.valueOf(i));
        }
        return bytes;
    }

    private boolean ifStringIsABytecodeChecker(String stringToCheck) {
        stringToCheck = stringToCheck.replaceAll("\\s+", "");
        String[] arrayToRemove = stringToCheck.split("");
        for (String i : arrayToRemove) {
            try {
                byte b = Byte.parseByte(i);
            } catch (NumberFormatException numberFormatException) {
                return false;
            }
        }
        return true;
    }

    private List<File> ifMatchingFileExist(String path, String extension, List<File> filesList) {
        File directoryPath = new File(path);
        File[] files = directoryPath.listFiles();
        if (files != null)
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith("." + extension)) {
                    filesList.add(file);
                } else if (file.isDirectory()) {
                    ifMatchingFileExist(file.getAbsolutePath(), extension, filesList);
                }
            }
        return filesList;
    }

    private String message(int m) {
        if (m % 10 == 1) return m + " plik zmodyfikowano pomyślnie.";
        if ((m % 10 == 2) || (m % 10 == 3) || (m % 10 == 4)) return m + " pliki zmodyfikowano pomyślnie.";
        else return m + " plików nie zmodyfikowano.";
    }

}
