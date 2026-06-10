/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.modelo;

import org.modelmapper.ModelMapper;
import org.ubp.edu.ar.ejemplocompletofx.dao.Dao;

/**
 *
 * @author agustin
 */
public abstract class Modelo {
    public Dao dao;
    public ModelMapper mapper = new ModelMapper();
}
