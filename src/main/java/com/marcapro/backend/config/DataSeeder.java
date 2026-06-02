package com.marcapro.backend.config;

import com.marcapro.backend.entity.AdminUser;
import com.marcapro.backend.entity.NearbyPoint;
import com.marcapro.backend.entity.Property;
import com.marcapro.backend.enums.PropertyStatus;
import com.marcapro.backend.enums.PropertyType;
import com.marcapro.backend.repository.AdminUserRepository;
import com.marcapro.backend.repository.NearbyPointRepository;
import com.marcapro.backend.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PropertyRepository propertyRepository;
    private final NearbyPointRepository nearbyPointRepository;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedProperties();
    }

    private void seedAdmin() {
        if (adminUserRepository.count() > 0) return;
        AdminUser admin = new AdminUser();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("MarcaPro Admin");
        admin.setActive(true);
        adminUserRepository.save(admin);
    }

    private void seedProperties() {
        if (propertyRepository.count() > 0) return;

        // Propiedad 1 — Casa campestre en Chía
        Property p1 = new Property();
        p1.setTitle("Casa campestre con piscina en Chía");
        p1.setDescription("""
            Hermosa casa campestre ubicada en la mejor zona de Chía, con vista a la montaña y total tranquilidad.

            La propiedad cuenta con amplios jardines, piscina privada, zona de BBQ y estacionamiento cubierto para 3 vehículos.
            Acabados en madera importada, cocina integral y cuarto de servicio independiente.

            A 5 minutos del centro de Chía y 35 minutos de Bogotá por autopista Norte.
            """);
        p1.setPrice(new BigDecimal("980000000"));
        p1.setType(PropertyType.CASA);
        p1.setStatus(PropertyStatus.DISPONIBLE);
        p1.setAreaSqm(320.0);
        p1.setBedrooms(4);
        p1.setBathrooms(4);
        p1.setParkingSpots(3);
        p1.setFloors(2);
        p1.setAddress("Vereda La Balsa, km 3 vía Chía-Cajicá");
        p1.setCity("Chía");
        p1.setNeighborhood("La Balsa");
        p1.setLatitude(4.8680);
        p1.setLongitude(-74.0590);
        p1.setFeatured(true);
        p1.setCoverPhotoUrl("https://images.unsplash.com/photo-1613977257592-4871e5fcd7c4?w=800&q=80");
        p1.setPhotoUrls(List.of(
            "https://images.unsplash.com/photo-1613977257592-4871e5fcd7c4?w=800&q=80",
            "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800&q=80",
            "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=800&q=80",
            "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=800&q=80"
        ));
        Property saved1 = propertyRepository.save(p1);
        nearbyPointRepository.saveAll(List.of(
            nearby(saved1, "Centro Comercial Fontanar", "Centro comercial", 4.8732, -74.0421, 2100.0),
            nearby(saved1, "Colegio Gimnasio Los Portales", "Colegio", 4.8650, -74.0520, 600.0),
            nearby(saved1, "Clínica Chía", "Clínica", 4.8613, -74.0601, 950.0),
            nearby(saved1, "Parque Principal Chía", "Parque", 4.8619, -74.0592, 800.0),
            nearby(saved1, "Éxito Chía", "Supermercado", 4.8643, -74.0445, 1500.0)
        ));

        // Propiedad 2 — Apartamento moderno en Cajicá
        Property p2 = new Property();
        p2.setTitle("Apartamento moderno en conjunto cerrado – Cajicá");
        p2.setDescription("""
            Apartamento de 3 habitaciones en conjunto cerrado con excelentes zonas comunes.

            Cocina abierta al living, balcón con vista a zonas verdes, cuarto de lavado independiente.
            El conjunto cuenta con piscina, gimnasio, cancha de squash y zona de juegos para niños.

            Excelente inversión — arrendamiento promedio zona: $2.200.000/mes.
            """);
        p2.setPrice(new BigDecimal("485000000"));
        p2.setType(PropertyType.APARTAMENTO);
        p2.setStatus(PropertyStatus.DISPONIBLE);
        p2.setAreaSqm(95.0);
        p2.setBedrooms(3);
        p2.setBathrooms(2);
        p2.setParkingSpots(1);
        p2.setFloors(1);
        p2.setAddress("Carrera 5 # 3A-20, Conjunto Praderas del Norte");
        p2.setCity("Cajicá");
        p2.setNeighborhood("Praderas del Norte");
        p2.setLatitude(4.9166);
        p2.setLongitude(-74.0275);
        p2.setFeatured(true);
        p2.setCoverPhotoUrl("https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800&q=80");
        p2.setPhotoUrls(List.of(
            "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800&q=80",
            "https://images.unsplash.com/photo-1565182999561-18d7dc61c393?w=800&q=80",
            "https://images.unsplash.com/photo-1484154218962-a197022b5858?w=800&q=80"
        ));
        Property saved2 = propertyRepository.save(p2);
        nearbyPointRepository.saveAll(List.of(
            nearby(saved2, "Centro Comercial Cajamar", "Centro comercial", 4.9200, -74.0220, 800.0),
            nearby(saved2, "Colegio Liceo de Cervantes", "Colegio", 4.9180, -74.0300, 400.0),
            nearby(saved2, "Hospital de Cajicá", "Hospital", 4.9185, -74.0265, 200.0),
            nearby(saved2, "Parque Cajicá", "Parque", 4.9172, -74.0262, 350.0)
        ));

        // Propiedad 3 — Lote en Zipaquirá
        Property p3 = new Property();
        p3.setTitle("Lote urbanizable en sector de alto desarrollo – Zipaquirá");
        p3.setDescription("""
            Lote de 600m² en sector residencial consolidado con todos los servicios.

            Terreno plano, escrituras al día, sin limitaciones de construcción.
            Ideal para casa familiar o pequeño proyecto de vivienda.

            A 3 cuadras de la vía principal y 10 minutos de la Catedral de Sal.
            """);
        p3.setPrice(new BigDecimal("320000000"));
        p3.setType(PropertyType.LOTE);
        p3.setStatus(PropertyStatus.DISPONIBLE);
        p3.setAreaSqm(600.0);
        p3.setAddress("Calle 8 # 12-40, Urbanización Los Cedros");
        p3.setCity("Zipaquirá");
        p3.setNeighborhood("Los Cedros");
        p3.setLatitude(5.0230);
        p3.setLongitude(-74.0060);
        p3.setFeatured(false);
        p3.setCoverPhotoUrl("https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800&q=80");
        p3.setPhotoUrls(List.of(
            "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800&q=80",
            "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&q=80"
        ));
        Property saved3 = propertyRepository.save(p3);
        nearbyPointRepository.saveAll(List.of(
            nearby(saved3, "Catedral de Sal", "Turístico", 5.0224, -74.0068, 800.0),
            nearby(saved3, "Alcaldía Zipaquirá", "Gobierno", 5.0220, -74.0058, 500.0),
            nearby(saved3, "Supermercado Olímpica", "Supermercado", 5.0215, -74.0075, 600.0)
        ));

        // Propiedad 4 — Local comercial en Chía
        Property p4 = new Property();
        p4.setTitle("Local comercial esquinero – Alto flujo peatonal Chía");
        p4.setDescription("""
            Local en esquina de alto tráfico peatonal y vehicular, a una cuadra del parque principal de Chía.

            30m² de área útil, baño propio, depósito, vitrina doble. Actualmente arrendado en $1.800.000/mes.
            Excelente rentabilidad: 4.5% anual sobre el valor comercial.

            Documento en regla, entrega inmediata si se requiere.
            """);
        p4.setPrice(new BigDecimal("480000000"));
        p4.setType(PropertyType.LOCAL_COMERCIAL);
        p4.setStatus(PropertyStatus.DISPONIBLE);
        p4.setAreaSqm(30.0);
        p4.setAddress("Carrera 11 # 12-03 Esquina, Centro");
        p4.setCity("Chía");
        p4.setNeighborhood("Centro");
        p4.setLatitude(4.8620);
        p4.setLongitude(-74.0600);
        p4.setFeatured(true);
        p4.setCoverPhotoUrl("https://images.unsplash.com/photo-1604328698692-f76ea9498e76?w=800&q=80");
        p4.setPhotoUrls(List.of(
            "https://images.unsplash.com/photo-1604328698692-f76ea9498e76?w=800&q=80",
            "https://images.unsplash.com/photo-1497366216548-37526070297c?w=800&q=80"
        ));
        Property saved4 = propertyRepository.save(p4);
        nearbyPointRepository.saveAll(List.of(
            nearby(saved4, "Parque Principal Chía", "Parque", 4.8619, -74.0592, 100.0),
            nearby(saved4, "Banco de Bogotá", "Banco", 4.8622, -74.0588, 50.0),
            nearby(saved4, "Iglesia San Pedro", "Iglesia", 4.8617, -74.0596, 120.0)
        ));

        // Propiedad 5 — Casa en Cajicá reservada
        Property p5 = new Property();
        p5.setTitle("Casa de dos pisos en conjunto residencial – Cajicá");
        p5.setDescription("""
            Amplia casa en conjunto residencial privado con vigilancia 24 horas.

            Sala-comedor integrados, cocina integral, estudio en primer piso.
            Tres habitaciones en segundo piso, habitación principal con vestier y baño privado.
            Patio posterior con zona de ropas y jardín.
            """);
        p5.setPrice(new BigDecimal("620000000"));
        p5.setType(PropertyType.CASA);
        p5.setStatus(PropertyStatus.RESERVADO);
        p5.setAreaSqm(160.0);
        p5.setBedrooms(3);
        p5.setBathrooms(3);
        p5.setParkingSpots(2);
        p5.setFloors(2);
        p5.setAddress("Calle 15 # 8-22, Conjunto Las Margaritas");
        p5.setCity("Cajicá");
        p5.setNeighborhood("Las Margaritas");
        p5.setLatitude(4.9150);
        p5.setLongitude(-74.0300);
        p5.setFeatured(false);
        p5.setCoverPhotoUrl("https://images.unsplash.com/photo-1576941089067-2de3c901e126?w=800&q=80");
        p5.setPhotoUrls(List.of(
            "https://images.unsplash.com/photo-1576941089067-2de3c901e126?w=800&q=80",
            "https://images.unsplash.com/photo-1583608205776-bfd35f0d9f83?w=800&q=80"
        ));
        propertyRepository.save(p5);
    }

    private NearbyPoint nearby(Property property, String name, String category,
                                double lat, double lng, double dist) {
        NearbyPoint np = new NearbyPoint();
        np.setProperty(property);
        np.setName(name);
        np.setCategory(category);
        np.setLatitude(lat);
        np.setLongitude(lng);
        np.setDistanceMeters(dist);
        return np;
    }
}
