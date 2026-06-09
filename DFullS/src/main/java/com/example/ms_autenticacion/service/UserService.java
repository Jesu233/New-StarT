package com.example.ms_autenticacion.service;

import com.example.ms_autenticacion.model.User;
import com.example.ms_autenticacion.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; //encriptar passwrd

    //transforma el rut ej: 12.000.000-0 y derivados en 120000000
    private String limpiarRun(String run) {
        if (run == null) return null;
        return run.replace(".", "")
                .replace("-", "")
                .replace(" ", "")
                .toUpperCase();
    }

    //registro completo
   public User register(String run,String nombre,String apellido, String email, String password, String role){
        String runlimpio = limpiarRun(run);
        log.info("Intentando registrar usuario con RUN: {}", run);

        if (userRepository.existsById(runlimpio)) {
            log.warn("Resgistro fallido: RUN {} ya existe", runlimpio);
            throw new RuntimeException("Error: El RUN " + runlimpio + " ya está registrado.");
        }
        if (userRepository.findByEmail(email).isPresent()){
            log.warn("Registro fallido: email {} ya etsá en uso", email);
            throw new RuntimeException("Error: El email " + email + " ya está en uso.");
        }

        User user = User.builder()
                .run(runlimpio)
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role != null ? role : "USER")
                .build();
        User saved = userRepository.save(user);
        log.info("Usuario registrado exitosamente con RUN {}", runlimpio);

        return saved;
   }

    //registro mas simple que asigna el rol de usuario de forma inmediata
    public User registerSimple(String run, String nombre, String apellido, String email, String password, String role){
        return  register(run, nombre, apellido, email, password, "USER");
    }
    public Optional<User> findByRun (String run){
        return userRepository.findById(limpiarRun(run));
    }

    //Actualizar usuario
    public User update(String run, User userDetails){
        String runlimpio = limpiarRun(run);
        log.info("Actualizando usuario con RUN: {}", runlimpio);

        User user = userRepository.findById(runlimpio)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado."));

        Optional<User> emailDuplicado = userRepository.findByEmail(userDetails.getEmail());

        if (emailDuplicado.isPresent() && !emailDuplicado.get().getRun().equals(runlimpio)){
            throw new RuntimeException("Error: el email " + userDetails.getEmail() + " ya está registrado por otro usuario.");

        }
        user.setNombre(userDetails.getNombre());
        user.setApellido(userDetails.getApellido());
        user.setEmail(userDetails.getEmail());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()){
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));

        }
        log.info("Usuario {} actualizado correctamente", runlimpio);
        return userRepository.save(user);
    }

    public void delete(String run){
        String runlimpio = limpiarRun(run);
        log.info("Eliminando usuario con RUN: {}", runlimpio);

        if(!userRepository.existsById(runlimpio)){
            log.info("Intento de eliminar usuario inexistente: {}", runlimpio);
            throw new RuntimeException("No se puede eliminar.");
        }
        userRepository.deleteById(runlimpio);
        log.info("Usuario {} eliminado correctamente", runlimpio);
    }

    public Optional<User> findByNombre(String nombre){return userRepository.findByNombre(nombre);
    }
}
