

## Scripts paralelos
Los archivos contenidos dentro del directorio `scripts` realizan tareas de obtención o procesamiento de datos que no ajenas al flujo de colección de datos por Crowsourcing. A continuación los archivos y una breve descripción:

- `PlumeLabs_PollutantsCollector.jl`:
Este script se encarga de descargar los últimos datos de contaminantes desde la nube de PlumeLabs. Es importante notar que este archivo contiene la lista de sensores por los que va a preguntar.

- `PlumeLabs_PositionsCollector.jl`:
Se encarga de obtener los datos de las posiciones registradas en una ventana de tiempo y de una lista data de sensores (también contenida en el script).

- `Evidences_Preprocessor.jl`:
El objetivo de este script es consultar a la base de datos por las últimas encuestas registradas y añadir su contenido al archivo `surveys_resume.csv` para que puedan ser visualizadas.

### Dependencia :heavy_exclamation_mark: :
Los datos procesados o descargados por estos scripts son delimitados por una ventada de tiempo. El archivo `parallel_descriptor.json` contiene las instrucciones de `start-timestamp` para cada script (lado izquierdo de la ventana de tiempo), mientras que los scripts obtienen el lado derecho al preguntar internamente por el timestamp del sistema. 


### Ejecución :arrow_forward: : 
Para ejecutar estos scripts en segundo plano y evitar que estos se detengan al cerrar la sesión SSH, se utiliza el siguiente comando:

`nohup nice julia script_name.jl >> script_name.log 2> script_name.err < /dev/null &`