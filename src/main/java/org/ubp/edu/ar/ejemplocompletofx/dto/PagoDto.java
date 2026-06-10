package org.ubp.edu.ar.ejemplocompletofx.dto;

import java.util.Date;
import org.ubp.edu.ar.ejemplocompletofx.modelo.FormaPago;

public class PagoDto {

    private int id;
    private Date fechaHora;
    private FormaPago formaPago;
    private float monto;

    public PagoDto() {
    }

    public PagoDto(Date fechaHora, FormaPago formaPago, float monto) {
        this.fechaHora = fechaHora;
        this.formaPago = formaPago;
        this.monto = monto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }
}
