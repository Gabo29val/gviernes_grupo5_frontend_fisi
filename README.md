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

*   **Importante!!** , cambiar la dirección IP que se encuentra en la clase RetrofitClient.kt y Payment.kt por su dirección IP.
    
    Ruta en el repositorio remoto de RetrofitClient.kt:
    https://github.com/Gabo29val/gviernes_grupo5_frontend_fisi/blob/development/app/src/main/java/com/example/dsm_frontend/api/RetrofitClient.kt
    
    **Captura**
    ![ip retrofit](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/ip_retrofit.PNG?alt=media&token=05d879b7-4f9e-42bd-99b0-354d537a45fd)
    
    Ruta en el repositorio remoto de Payment.kt
    https://github.com/Gabo29val/gviernes_grupo5_frontend_fisi/blob/development/app/src/main/java/com/example/dsm_frontend/api/Payment.kt
    
    **Captura**
    ![ip payment](https://firebasestorage.googleapis.com/v0/b/minimarket-de-barrio.appspot.com/o/ip_payment.PNG?alt=media&token=930de271-68d1-45c2-a618-233d22618e8e)

*   Antes de ejecutar el proyecto del frontend se debe tener descargado y en ejecución el backend que se encuentra en el siguiente enlace:
    
    https://github.com/Gabo29val/gviernes_grupo5_backend_fisi.git

*   Ahora sí ya puede ejecutar el proyecto del frontend.

### Observación

*   Al ejecutar la aplicación, ya sea en el emulador o en su celular, no debe estar en modo noche.
