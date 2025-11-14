//Người làm: Nguyễn Cao Việt An
package entity;

import java.util.Objects;

public class Thue {
    private String maThue;
    private String tenThue;
    private double tyLeThue;
    private String moTa;

    public Thue(String maThue, String tenThue, double tyLeThue, String moTa) {
        this.maThue = maThue;
        this.tenThue = tenThue;
        this.tyLeThue = tyLeThue;
        this.moTa = moTa;
    }

    public Thue() {
    }
    
    public Thue(String maThue) {
        this.maThue = maThue;
    }

    public String getMaThue() {
        return maThue;
    }

    public void setMaThue(String maThue) {
        this.maThue = maThue;
    }

    public String getTenThue() {
        return tenThue;
    }

    public void setTenThue(String tenThue) {
        this.tenThue = tenThue;
    }

    public double getTyLeThue() {
        return tyLeThue;
    }

    public void setTyLeThue(double tyLeThue) {
        this.tyLeThue = tyLeThue;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Thue other = (Thue) obj;
        return Objects.equals(maThue, other.maThue);
    }

    @Override
    public String toString() {
        return "Thue [maThue=" + maThue + ", tenThue=" + tenThue + ", tyLeThue=" + tyLeThue + ", moTa=" + moTa + "]";
    }
}