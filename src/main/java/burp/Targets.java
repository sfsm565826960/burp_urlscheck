package burp;

import java.util.ArrayList;
import java.util.regex.*;

public class Targets {
    public ArrayList<Object[]> targets;
    public String excludeSuffix = "html|js|css|ico|svg|jpg|jpeg|pdf|png|gif|bmp|zip|rar|woff|woff2|doc|docx|xls|xlsx";
    public Pattern excludeSuffixRegex;

    public Targets() {
        this(100);
    }

    public Targets(int size) {
        targets = new ArrayList<Object[]>(size);
        excludeSuffixRegex = Pattern.compile(String.format("[\\w]+[\\.](%s)", excludeSuffix), Pattern.CASE_INSENSITIVE);
    }

    public void updateExcludeSuffix(String text) {
        excludeSuffix = text;
        excludeSuffixRegex = Pattern.compile(String.format("[\\w]+[\\.](%s)", excludeSuffix), Pattern.CASE_INSENSITIVE);
    }

    public boolean isExcludeSuffix(String url) {
        return excludeSuffixRegex.matcher(url).matches();
    }

    public Object[] add(Target target) {
        return this.add(target.getObjectArray(targets.size()));
    }

    public Object[] add(Object[] data) {
        if (data.length != 4) return null;
        data[0] = targets.size();
        targets.add(data);
        return data;
    }

    public Object[] add(String text) {
        return add(new Target(text));
    }

    public Object[] getObject(int index) {
        return targets.get(index);
    }

    public Target getTarget(int index) {
        return new Target(targets.get(index));
    }

    public void remove(int index) {
        targets.remove(index);
    }

    public void edit(int index, Target target) {
        this.edit(index, target.getObjectArray(index));
    }

    public void edit(int index, Object[] data) {
        if (data.length != 4 || index < 0 || index >= targets.size()) return;
        data[0] = index;
        targets.set(index, data);
    }

    public int size() {
        return targets.size();
    }

    public boolean contains(String text) {
        for (int i = 0; i < targets.size(); i++ ) {
            String targetText = (String) targets.get(i)[3];
            if (targetText.contains(text)) return true;
        }
        return false;
    }

    public Object[][] getArray(){
        return targets.toArray(new Object[targets.size()][]);
    }
}
