/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.factories;

import java.lang.reflect.InvocationTargetException;
import org.ubp.edu.ar.ejemplocompletofx.modelo.Modelo;

/**
 *
 * @author agustin
 */
public final class FabricaModelo {
    
    private FabricaModelo() {
    
    }
    
    public static Modelo fabricar(String nombreClase) {
        Modelo modelo = null;
        try {               
            modelo = (Modelo) Class.forName(Modelo.class.getPackage().getName() + "." + nombreClase)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.out.println(e.toString());
        }
        return modelo;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CloneNotSupportedException();
    }
    
    
}
