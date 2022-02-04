---
Título del proyecto: Minimarket de barrio
Autor: G5 - UNMSM
Curso: Desarrollo de Sistemas Móviles
Docente: Bruno Palacios - bfpalacios@gmail.com / Felipe Landa - cristianlanda@java.com.pe
---

Rama development (rama principal de desarrollo frontend)
==
Esta rama contiene el proyecto principal del **frontend** de la aplicación "Minimarket de barrio".

### Herramientas utilizadas

*   **Android Studio**
 
    Android Studio proporciona las herramientas más rápidas para crear aplicaciones en todo tipo de dispositivo Android.
    
*   **Kotlin** 
    
    Crea mejores apps para Android más rápido con Kotlin, un lenguaje de programación moderno de tipo estático que usan más del 60% de los desarrolladores profesionales de         Android. Kotlin te ayuda a aumentar la productividad, la satisfacción de los desarrolladores y la seguridad del código.
  
*   **Retrofit**    
    Retrofit es una librería para Android y java compatible con Kotlin para hacer llamadas de red, obtener el resultado y “parsearlo” 
    de forma automática a su objeto, esto facilita mucho realizar peticiones a un API y procesar la respuesta.

    Link de Retrofit: https://square.github.io/retrofit/ 

*   **Stripe**
    
    Reunimos todo lo necesario para desarrollar sitios web y aplicaciones capaces de aceptar pagos y hacer transferencias en todo el mundo. 
    Los productos de Stripe impulsan pagos para minoristas que operan en Internet y en persona, empresas dedicadas a las suscripciones, 
    plataformas de software y marketplaces, y mucho más.
    
    Link de Stripe: https://stripe.com/es-419-us

### Guía de instalación

*   Clonar el repositorio usando el comando:

        $ git clone https://github.com/Gabo29val/gviernes_grupo5_frontend_fisi.git   
    
*   Abrir una terminal GitBash o desde Android Studio y traer los cambios de la rama remota **development** a una rama local.
    O solo verificar que ya se encuentra en la rama development en la interfaz de Android Studio.
    
    ![rama development](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/captura_rama_development.PNG?alt=media&token=01bc67a1-eee1-452e-a069-6173b7756edd)

*   Abrir el proyecto en Android Studio

*   **Importante!!** , cambiar la dirección IP que se encuentra en la clase RetrofitClient.kt por su dirección IP.
    
    Ruta en el repositorio remoto de RetrofitClient.kt:
    https://github.com/Gabo29val/gviernes_grupo5_frontend_fisi/blob/development/app/src/main/java/com/example/dsm_frontend/api/RetrofitClient.kt
    
    **Captura**
    ![ip retrofit](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/cambio_ip.PNG?alt=media&token=7febac64-de2e-46b6-a8ee-4a3f2c266ed7)

*   Antes de ejecutar el proyecto del frontend se debe tener descargado y en ejecución el backend que se encuentra en el siguiente enlace:
    
    https://github.com/Gabo29val/gviernes_grupo5_backend_fisi.git

*   Ahora sí ya puede ejecutar el proyecto del frontend.

### Observación

*   Al ejecutar la aplicación, ya sea en el emulador o en su celular, no debe estar en modo noche.

### Algunas capturas de la aplicación **Minimarket de barrio**

*   Pantalla inicial

    ![front inicial](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/front_1.PNG?alt=media&token=4ff0603a-90a0-4a87-b595-82d6bae852e3)

*   Búsqueda de productos
    
    Ingreso de productos a buscar y resultados de la búsqueda.
    
    ![busqueda3](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/busqueda3.PNG?alt=media&token=3f9b5bc4-3ed6-4b51-ad32-e9d18f11b12c)

*   Información del producto
    
    ![info1](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/info_prod_1.PNG?alt=media&token=1b00d37f-1670-4022-98e1-116426103b7f)

*   Agregar al carrito
    
    ![add car1](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/add_carrito_1.PNG?alt=media&token=453a01a0-5982-4b45-885f-f80fc3ffa5a8)

*   Visualizar tiendas cercanas

    ![vis store2](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/visualizar_tiendas_2.PNG?alt=media&token=5473c7b2-7924-41ab-b3a6-f1705665410a)

*   Detalles de una tienda
 
    ![det store1](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/detalles_tienda_1.PNG?alt=media&token=b9ee3151-533f-4587-ab32-ea8502a262c4)

*   Visualizar carrito de compras

    ![car2](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/carrito_2.PNG?alt=media&token=afc359b8-04cb-42ee-8e20-a4bfd4cf52a4)

*   Comprar

    ![buy1](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/pago_1.PNG?alt=media&token=f315ecaf-679c-4152-8c7b-896a05739fb0)
    ![buy2](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/pago_2.PNG?alt=media&token=ae083af5-a918-4dda-8a08-b538b1afad18)

