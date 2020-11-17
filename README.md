# Gvbr Desafio

## Dados upload entrada
  ### Request Params 
  @RequestParam("arquivo") MultipartFile file,
  
  @RequestParam("tipoDoArquivo") String tipoDocumento,
  
  @RequestParam("localArmazenamento") int localArmazenamento
  
## Dados entrada Download
  ### Request Params 
É verificado no BD, caso não exista verifica no storage

  @RequestParam("arquivo") String arquivo
  
## Opções validas para upload -> 
localArmazenamento 1 - Banco de dados, localArmazenamento 2 - Storage

#### Utilizado o firebase como storage, necessário gerar um key para utilizar, deve ser armazenada no diretório /key, também necessário alterar bucket name e project id na classe FirebaseStorageStrategy.
