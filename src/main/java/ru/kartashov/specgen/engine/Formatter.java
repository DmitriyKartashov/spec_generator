package ru.kartashov.specgen.engine;

import ru.kartashov.specgen.domain.MethodInfo;
import ru.kartashov.specgen.domain.Usage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Dmitrii Kartashov
 */
public class Formatter {
    Pattern ppPattern = Pattern.compile("(pp\\d\\d)");

    public String format(List<Usage> usageList)
    {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, List<Usage>> fieldUsageList = usageList.stream()
            .filter(this::isFieldAccessor)
            .collect(Collectors.groupingBy(this::getFieldName));
        stringBuilder.append("|field name|where retrieved|where populated|");
        stringBuilder.append("\n");
        fieldUsageList.entrySet().forEach(entry ->
        {
            stringBuilder.append("|");
            stringBuilder.append(entry.getKey());
            stringBuilder.append("|");
            List<Usage> usages = entry.getValue();
            stringBuilder.append(usages.stream()
                .filter(u -> u.getMethodInfo().isGetter())
                .map(u -> ppPattern.matcher(u.getJarName()))
                .filter(Matcher::find)
                .map(m -> m.group(0))
                .distinct()
                .collect(Collectors.joining(", ")));
            stringBuilder.append("|");
            stringBuilder.append(usages.stream()
                .filter(u -> !u.getMethodInfo().isGetter())
                .map(u -> ppPattern.matcher(u.getJarName()))
                .filter(Matcher::find)
                .map(m -> m.group(0))
                .distinct()
                .collect(Collectors.joining(", ")));
            stringBuilder.append("|");
            stringBuilder.append("\n");
        });
        return stringBuilder.toString();
    }

    private String getFieldName(Usage u) {
        return u.getMethodInfo().getMethodName().substring(3);
    }

    private boolean isFieldAccessor(Usage u) {
        String methodName = u.getMethodInfo().getMethodName();
        return methodName.startsWith("get") || methodName.startsWith("set");
    }


    public static void main(String[] args) throws Exception {
        InputGatherer i = new InputGatherer();
        List<MethodInfo> m = i.visitJar("D:/work/spec_generator/xom/target/xom-1.0-SNAPSHOT.jar", "Posting");
        OutputGatherer outputGatherer = new OutputGatherer(m);
        List<Usage> allUsages = new ArrayList<>();
        allUsages.addAll(outputGatherer.visitJar("D:\\work\\spec_generator\\pp77\\target\\pp77-1.0-SNAPSHOT.jar"));
        allUsages.addAll(outputGatherer.visitJar("D:\\work\\spec_generator\\pp66\\target\\pp66-1.0-SNAPSHOT.jar"));
        System.out.println(new Formatter().format(allUsages));
    }
}
