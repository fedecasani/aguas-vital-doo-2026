/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ubp.edu.ar.ejemplocompletofx.factories;

import java.lang.reflect.InvocationTargetException;
import org.ubp.edu.ar.ejemplocompletofx.dao.Dao;

/**
 *
 * @author agustin
 */
public final class FabricaDao {
    
    private FabricaDao() {
        
    }
    
    public static Dao fabricar(String nombreClase) {
        Dao dao = null;
        try {               
            dao = (Dao) Class.forName(Dao.class.getPackage().getName() + "." + nombreClase)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            System.out.println(e.toString());
        }
        return dao;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CloneNotSupportedException();
    }
    
    
    
}
