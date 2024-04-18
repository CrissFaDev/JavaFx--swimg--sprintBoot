package gm.tienda_libros;

import gm.tienda_libros.presentacion.libroForm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class TiendaLibrosApplication {

	public static void main(String[] args) {
		/*SpringApplication.run(TiendaLibrosApplication.class, args); */

		ConfigurableApplicationContext contextoSpring =
				new SpringApplicationBuilder(TiendaLibrosApplication.class)
						.headless(false)
						.web(WebApplicationType.NONE)
						.run(args);
		// Ejecutamos el codigo para cargar el formulario
		EventQueue.invokeLater(() -> {
			//Obtenemos el objeto form atravez de spring
			libroForm libroform = contextoSpring.getBean(libroForm.class);
			libroform.setVisible(true);
		});
	}

}
