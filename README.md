#ms-playlist
API responsavel por informações referentes à listas musicais

##Pre-requisitos:
	- JDK 11
	- Maven
	- Docker

##Inicializar projeto:
**Clone project**

```sh
git clone git@github.com:shudalla/ms-playlist.git

**Comandos**
```sh
Na raiz do projeto:

mvn clean install
mvn docker:build

A aplicação esta configurada para http://localhost:9090/ para o perfil de DEV e 8002 para o padrão


**Testes unitarios**
Na rapiz do projeto:

```sh
mvn test
```

**Visualizar relatorios jacoco**
Na pasta {projeto}/target/site/jacoco/, abra o arquivo index.html

**Documentação**
Na pasta main/resource/swagger/