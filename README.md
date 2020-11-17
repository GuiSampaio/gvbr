# Gvbr Desafio

## Dados upload entrada
  ### Request Params 
  @RequestParam("arquivo") MultipartFile file, -> arquivo
  
  @RequestParam("tipoDoArquivo") String tipoDocumento, -> tipo do arquivo Ex* identidade, licença e etc...
 
  
  @RequestParam("localArmazenamento") int localArmazenamento -> opção de armazenamento
  
## Dados entrada Download
  ### Request Params 
É verificado no BD, caso não exista verifica no storage

  @RequestParam("arquivo") String arquivo -> nome do arquivo upado
  
## Opções validas para upload -> 
localArmazenamento 1 - Banco de dados, localArmazenamento 2 - Storage

#### Utilizado o firebase como storage, necessário gerar um key para utilizar, deve ser armazenada no diretório /key, também necessário alterar bucket name e project id na classe FirebaseStorageStrategy.
