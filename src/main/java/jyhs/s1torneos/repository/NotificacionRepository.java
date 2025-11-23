package jyhs.s1torneos.repository;

import jyhs.s1torneos.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findNotificacionByUsuario_Id(Long usuarioId);

    Notificacion findNotificacionById(Long id);
}
