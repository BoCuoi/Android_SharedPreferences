package nguyenlexuantung.extendstorageapp;

public class Contact {
    private String Address;
    private String Name;
    private String Phone;

    public Contact() {
    }

    public Contact(String address, String name, String phone ) {
        Name = name;
        Phone = phone;
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @Override
    public String toString() {
        return
                "Name'" + Name + '\'' +
                        ", Phone'" + Phone + '\'' +
                        ", Address'" + Address;
    }
}
