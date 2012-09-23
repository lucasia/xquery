package com.lucasia.xquery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: ialucas
 */
public class XQueryFileReader {

    public String readFile(final String filePath) throws IOException {
        return readFile(filePath, new XQuery.AlwaysTrue<String>());
    }

    /**
     * exclude the namespace imports and definitions, taking care of those elsewhere
     */
    public String readFileExcludeNamespace(final String filePath) throws IOException {
        final XQuery.Predicate<String> excludeNamespaceLine = new XQuery.Predicate<String>() {

            @Override
            boolean eval(final String line) {

                if (!line.contains("namespace")) return true;

                String str = line.replace(" ", "");

                // using startsWith in case these words appear in the code elsewhere
                // e.g. strings, comments, xml tags
                return !(str.startsWith("modulenamespace") ||
                        str.startsWith("importmodulenamespace") ||
                        str.startsWith("declarenamespace"));
            }
        };

        return readFile(filePath, excludeNamespaceLine);
    }

    public String readFile(String filePath, final XQuery.Predicate<String> shouldAppend) throws IOException {
        filePath = filePath.replace("file:", "");

        final FileReader fileReader = new FileReader(filePath);

        final BufferedReader bufferedReader = new BufferedReader(fileReader);

        final StringBuffer buffer = new StringBuffer();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (shouldAppend.eval(line)) {
                buffer.append(line).append("\n");
            }
        }

        fileReader.close();

        return buffer.toString();
    }
}
