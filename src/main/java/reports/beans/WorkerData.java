package reports.beans;

public class WorkerData {
    private String name;
    private String surname;
    private String prof;
    private int totalEarnings;
    private int clientCnt;

    public WorkerData(String name_, String surname_, String prof_,
                      int totalEarnings_, int clientCnt_)
    {
        name = name_;
        surname = surname_;
        prof = prof_;
        totalEarnings = totalEarnings_;
        clientCnt = clientCnt_;
    }
    public String getName() {
        return name;
    }
    public void setName(String name_) {
        name = name_;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname_) {
        surname = surname_;
    }
    public String getProf() {
        return prof;
    }
    public void setProf(String prof_) {
        prof = prof_;
    }
    public int getTotalEarnings() {
        return totalEarnings;
    }
    public void setTotalEarnings(int totalEarnings_) {
        totalEarnings = totalEarnings_;
    }
    public int getClientCnt() {
        return clientCnt;
    }
    public void setClientCnt(int clientCnt_) {
        clientCnt = clientCnt_;
    }
}
