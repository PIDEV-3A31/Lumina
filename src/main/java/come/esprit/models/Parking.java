package come.esprit.models;

public class Parking {
    private int id_parck;
    private String name_parck;
    private int capacity;
    private String status_parking;
    private String adresses;
    private String tarif;
    private int places_reservees;



    public int getId_parck() {
        return id_parck;
    }

    public void setId_parck(int id_parck) {
        this.id_parck = id_parck;
    }

    public String getName_parck() {
        return name_parck;
    }

    public void setName_parck(String name_parck) {
        this.name_parck = name_parck;
    }

    public int getCapacity() {
        return capacity;  // Retourner un entier
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;  // Assigner un entier
    }

    public String getStatus_parking() {
        return status_parking;
    }

    public void setStatus_parking(String status_parking) {
        this.status_parking = status_parking;
    }

    public String getAdresses() {
        return adresses;
    }

    public void setAdresses(String adresses) {
        this.adresses = adresses;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }
    public int getPlaces_reservees(){
        return places_reservees;
    }
    public void setPlaces_reservees(int places_reservees){
        this.places_reservees = places_reservees;
    }

    public Parking() {
    }


    public Parking(String name_parck, int capacity, String status_parking, String adresses, String tarif, int places_reservees) {
        this.name_parck = name_parck;
        this.capacity = capacity;
        this.status_parking = status_parking;
        this.adresses = adresses;
        this.tarif = tarif;
        this.places_reservees=places_reservees;
    }


    @Override
    public String toString() {
        return "Parking{" +
                "id_parck=" + id_parck +
                ", name_parck='" + name_parck + '\'' +
                ", capacity='" + capacity + '\'' +
                ", status_parking='" + status_parking + '\'' +
                ", adresses='" + adresses + '\'' +
                ", tarif='" + tarif + '\'' +
                ", places_reservees=" + places_reservees +
                '}';
    }



    //  public int getPlace_reservees() {
 //   }
}





