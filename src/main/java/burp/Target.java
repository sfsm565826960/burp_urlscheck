package burp;

import java.util.ArrayList;

public class Target {
    public boolean isChecked;
    public boolean isVul;
    public String text;

    public Target(String text) {
        this.isChecked = false;
        this.isVul = false;
        this.text = text;
    }

    public Target(Object[] data) {
        switch(data.length) {
            case 4:
                this.isChecked = (boolean) data[1];
                this.isVul = (boolean) data[2];
                this.text = (String) data[3];
                break;
            case 3:
                this.isChecked = (boolean) data[0];
                this.isVul = (boolean) data[1];
                this.text = (String) data[2];
                break;
            default:
                this.isChecked = false;
                this.isVul = false;
                this.text = "";
        }
    }

    public Object[] getObjectArray(int index) {
        ArrayList<Object> data = new ArrayList<Object>();
        data.add(index);
        data.add(this.isChecked);
        data.add(this.isVul);
        data.add(this.text);
        return data.toArray();
    }

    public Object[] getObjectArray() {
        return this.getObjectArray(0);
    }
}
