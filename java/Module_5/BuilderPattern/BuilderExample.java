package BuilderPattern;

class Computer{
    private String CPU;
    private int RAM;

    private int storage;
    private boolean graphicsCard;
    private boolean wifi;
    private boolean bluetooth;

    private Computer(ComputerBuilder builder){
        this.CPU=builder.CPU;
        this.RAM=builder.RAM;
        this.storage=builder.storage;
        this.graphicsCard=builder.graphicsCard;
        this.wifi=builder.wifi;
        this.bluetooth=builder.bluetooth;
    }

    public String toString() {
        return "Computer [CPU=" + CPU + ", RAM=" + RAM +
               ", storage=" + storage + "GB, graphicsCard=" + graphicsCard +
               ", wifi=" + wifi + ", bluetooth=" + bluetooth + "]";
    }

    public static class ComputerBuilder{
        private String CPU;
        private int RAM;

        private int storage=256;
        private boolean graphicsCard=false;
        private boolean wifi = false;
        private boolean bluetooth = false;

        public ComputerBuilder(String CPU,int RAM){
            this.CPU=CPU;
            this.RAM=RAM;
        }
        public ComputerBuilder setStorage(int storage){
            this.storage=storage;
            return this;
        }
        public ComputerBuilder setGraphicsCard(boolean graphicsCard){
            this.graphicsCard=graphicsCard;
            return this;
        }
        public ComputerBuilder setWifi(boolean wifi){
            this.wifi=wifi;
            return this;
        }
        public ComputerBuilder setBluetooth(boolean bluetooth){
            this.bluetooth = bluetooth;
            return this;
        }
        public Computer build(){
            return new Computer(this);
        }
    }
}

public class BuilderExample {
    public static void main(String args[]){
        Computer c1 = new Computer.ComputerBuilder("Intel", 32).build();
        System.out.println(c1);
    }
}
