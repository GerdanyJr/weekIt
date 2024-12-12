# WeekIt

Este repositório é dedicado ao minicurso: Construindo uma API Rest utilizando Java e Spring Boot, ministrado durante a décima edição da semana da tecnologia da informação [WeekIT](https://www.instagram.com/weekit.vdc/)
pelos discentes [Gerdany Junior](https://www.linkedin.com/in/gerdanyjr/) e [Murilo Anjos](https://www.linkedin.com/in/mu-anjos/),
do curso de Bacharelado de Sistemas de Informação do IFBA Campus Vitória da Conquista.
<br> <br>
Dentro desta documentação é possível encontrar todo o material de apoio utilizado durante o minicurso, além disso no final do documento é possível encontrar um checklist contendo o passo a passo da codificação do sistema 
desenvolvido, ao clicar em cada item você será redirecionado para o commit ou pull request contendo o código necessário para dar seguimento ao minicurso.

## Definindo entidades utilizando Hibernate

Quando estamos lidando com aplicações que se comunicam com bancos de dados precisamos modelar as entidades do banco de dados em objetos, para que possamos recuperá-los e manuseá-los antes de persisti-los. 

Para isso utilizamos um ORM (Object Relational Mapper), que age como uma ponte entre o universo Orientado a Objetos e os Bancos de dados. Quando trabalhamos com Java, o principal ORM é o [Hibernate](https://hibernate.org/orm/), uma implementação do JPA (Java Persistence API) que permite o desenvolvimento de classes persistentes utilizando Orientação a objetos e todas as suas características, como herança, polimorfismo, associação e composição. 

Para definir entidades utilizando Hibernate usamos a anotação `@Entity` essa anotação nos permite criar uma tabela no banco de dados contendo os atributos da classe definida, vale ressaltar que toda entidade deve conter um identificador único(chave primária), para criar um id é necessário utilizar a anotação `@Id`:

```java
// imports
@Entity
public class Aluno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private Integer idade;
	
	// construtor(s), getters, setters
}
```

No exemplo acima, utilizamos a geração automática de id através da anotação `@GeneratedValue` além disso devemos informar qual será estratégia adotada para geração de Id’s, nesse caso optei por utilizar a estratégia IDENTITY, dentre as estratégias diferentes temos:

1. **AUTO**: Valor padrão, deixa o provedor de persistência escolher a estratégia mais adequada de acordo com o banco de dados;
2. **IDENTITY**: Delega ao banco de dados a geração do valor da chave primária, geralmente utiliza uma coluna auto-incrementada;
3. **SEQUENCE**: Utiliza uma sequência do banco de dados para gerar valores únicos. É especialmente útil para bancos de dados que suportam sequências, como Oracle e PostgreSQL;
4. **TABLE**: Usa uma tabela dedicada no banco de dados para gerar valores de chave primária. Essa estratégia é independente do banco de dados, mas pode ter implicações de desempenho devido ao acesso adicional ao banco.

[Clique aqui para ler mais sobre as estratégias de geração de chaves primárias.](https://www.devmedia.com.br/jpa-como-usar-a-anotacao-generatedvalue/38592)

Além disso podemos modificar os atributos da tabela do banco de dados utilizando a anotação `@Table` :

```java
// imports
@Entity
@Table(name = "alunos")
public class Aluno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private Integer idade;
	
	// construtor(s), getters, setters
}
```

Outra prática comum é utilizar a anotação `@Column` para adicionar especificações relacionadas as colunas do banco:

```java
@Column(length = 100, nullable = false)
private String nome;
```

### Definindo relacionamentos utilizando Hibernate

Quando trabalhamos com banco de dados relacionais, é muito comum que as entidades se relacionem, por exemplo: um Curso tem muitas Matérias, nesse caso temos um relacionamento de um pra muitos (1…n), para representar isso em bancos de dados utilizamos uma chave estrangeira (uma chave primária de outra tabela) dentro da tabela Matéria. Para representar isso utilizando o hibernate faríamos da seguinte forma:

```java
@Entity
@Table(name = "materias")
public class Materia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    
    // construtor(s), getters e setters
}
```

```java
@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Materia> materias;
}
```

Neste caso temos um relacionamento unidirecional, onde apenas uma das entidades tem conhecimento da outra, apenas a entidade Curso “sabe” da existência de Materia. 

Caso o quiséssemos estabelecer um relacionamento bidirecional, deveríamos adicionar um atributo Curso dentro do objeto matéria, além disse seria necessário utilizar a anotação `@ManyToOne` e informar qual é o atributo na tabela oposta (nesse caso a table Curso) que é responsável por fazer o mapeamento de matéria:

```java
@Entity
@Table(name = "materias")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "curso")
    private List<Materia> materias;

}
```

```java
@Entity
@Table(name = "cursos")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Curso curso;
}

```

## Repositories

Quando trabalhamos com aplicações que utilizam dados de bancos de dados, é essencial contar com uma camada da aplicação responsável por executar operações e se comunicar com o banco de dados. Essa camada é denominada Repository e ela tem o objetivo de abstrair a comunicação direta com o banco de dados, permitindo que a aplicação interaja com os dados de forma mais limpa e estruturada, para isso ela atua entre a aplicação e o banco de dados, sendo responsável por executar as operações de **CRUD** (Create, Read, Update, Delete)**.**

Com o [Spring Data](https://spring.io/projects/spring-data) a definição de um repositório é feita através de interfaces que estendem algum repositório implementado pelo framework, nesse caso utilizaremos o [JpaRepository](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html)<T, Id>, uma interface genérica que deve receber dois argumentos, sendo eles a entidade que será persistida ou recuperada e o tipo do Id dessa entidade:

```java
public interface MateriaRepository extends JpaRepository<Materia, Long> {
 
}
```

Fazendo isso teremos todos os métodos básicos de CRUD (como `save()`, `findById()`, `findAll()`, `deleteById()`, entre outros) já implementados pelo Spring Data, isso acontece porque por baixo dos panos o Spring Data já implementa esses métodos para que possamos focar nas regras de negócio e lógica da aplicação.

### Definindo métodos de consulta

Embora o Spring Data implemente alguns métodos de interação com o banco de dados para diminuir o código boilerplate, muitas vezes precisaremos de consultas personalizadas, baseados nas nossas necessidades, existem 3 principais formas de implementar consultas personalizadas com Spring data: 

1. Através do nome do método;
2. Utilizando JPQL;
3. Utilizando SQL.

### [Criando consultas pela nomenclatura do método](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html)

Para criar um consulta através do nome do método, devemos seguir uma estrutura especifica: findBy + nome do campo + operador + outros campos, por exemplo:

```java
// Obter alunos pelo nome
public List<Aluno> findByNome(String nome);

// Obter alunos pelo nome, baseado numa busca
public List<Aluno> findByNomeStartingWith(String nome);

// Encontrar matérias com carga horário dentro de um intervalo 
public List<Materia> findByCargaHorariaBetween(Integer min, Integer max)
```

### [Criando consultas utilizando JPQL](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.at-query)

Java Persistence Query Language é a linguagem de consulta do JPA, diferente do SQL tradicional o JPQL trabalha com entidades e os seus atributos ao invés de diretamente com tabelas e colunas, permitindo que as consultas sejam feitas utilizando o modelo de objetos da aplicação.

Para definir que um método utilizará uma consulta feita em JPQL utilizamos a anotação `@Query` e passamos dentro da anotação qual será a consulta a ser feita, para referenciar um parâmetro do método dentro da consulta utilizamos o :nomeDoParametro e para nomear esse parâmetro utilizamos a anotação `@Param(”nome”)`, levando em consideração os exemplos dados anteriormente teríamos:

```java
// Obter alunos pelo nome
@Query("SELECT a FROM Aluno a WHERE a.nome = :nome")
public List<Aluno> findByNome(@Param("nome") String nome);

// Obter alunos pelo nome, baseado numa busca
@Query("SELECT a FROM Aluno a WHERE a.nome LIKE %:nome%")
public List<Aluno> findByNomeStartingWith(@Param("nome")String nome);

// Encontrar matérias com carga horário dentro de um intervalo 
@Query("SELECT m FROM Materia m WHERE m.cargaHoraria BETWEEN :min AND :max")
public List<Materia> findByCargaHorariaBetween(@Param("min")Integer min, @Param("max")Integer max)
```

### [Criando consultas utilizando SQL](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.at-query.native)

Para criar consultas utilizando SQL, utilizamos o `@Query`  juntamente com o atributo `nativeQuery` vale ressaltar que trazendo essa abordagem devemos respeitar o nome das tabelas e não dos objetos:

```java
// Obter alunos pelo nome
@Query("SELECT a FROM Alunos a WHERE a.nome = :nome", nativeQuery = true)
public List<Aluno> findByNome(@Param("nome") String nome);

// Obter alunos pelo nome, baseado numa busca
@Query("SELECT a FROM Alunos a WHERE a.nome LIKE %:nome%", nativeQuery = true)
public List<Aluno> findByNomeStartingWith(@Param("nome") String nome);

// Encontrar matérias com carga horário dentro de um intervalo 
@Query("SELECT * FROM Materias m WHERE m.cargaHoraria BETWEEN :min AND :max", nativeQuery = true)
public List<Materia> findByCargaHorariaBetween(@Param("min")Integer min, @Param("max")Integer max)
```

## Services

A camada Service de uma aplicação MVC é uma camada adicional utilizada para intermediar a comunicação entre a Controller e Repository. Tem o propósito de concentrar as regras de negócio da aplicação, como funcionalidades e validações.

Quando se trata de aplicações Java utilizando Spring Framework, essa camada é representada por uma classe anotada com `@Service` → essa anotação é uma especialização da anotação `@Component` serve para especificar de forma explícita que nossa classe será um **Bean** do Spring. 

### O que é são Beans?

No contexto do Spring Boot, Beans são classes que serão instanciadas, montadas e gerenciadas pelo [**Spring Ioc container](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/beans.html),** mas qual é a finalidade de se usar um Bean? para compreender melhor precisamos conhecer os padrões Inversão de Controle e Injeção de Dependência.

**Inversão de Controle** é um princípio de desenvolvimento que prega tirar a responsabilidade de uma classe de instanciar as suas dependências, para isso é necessário passar essa responsabilidade para um agente externo, seja um framework ou até outra classe (nesse caso o Spring Framework).

**Injeção de Dependência** é um padrão de projeto que visa injetar as dependências usadas por uma classe ao invés de deixar essa com que a classe instancie elas. Para ler mais [clique aqui.](https://www.dio.me/articles/beans-entendendo-a-base-fundamental-do-spring-framework) 

Para criar uma Service dentro do Spring utilizamos a anotação `@Service` acima da declaração da classe, dessa forma delegamos a responsabilidade de instanciar e gerenciar as instâncias dessa classe ao Spring Framework.

Dentro da Service que utilizaremos teremos uma instância do repository que declaramos antes, pois o utilizaremos para acessar e persistir os dados tratados no banco de dados. Essa instância será considerada uma dependência da nossa Service, pois para executar as suas funções a service depende do repository, como discutimos anteriormente, quando trabalhamos com o Spring Framework utilizamos um padrão de projeto denominado **Injeção de Dependência,** permitindo que o Spring injete automaticamente as dependências utilizadas pelas nossas classes.

Existem três principais formas de aplicar injeção de dependência no Spring, utilizando injeção por construtor, por setter ou por atributo, nesse minicurso utilizaremos a injeção por construtor pelo fato de ser a mais recomendada, mas caso queira entender melhor as outras [clique aqui.](https://medium.com/cwi-software/os-benef%C3%ADcios-de-usar-inje%C3%A7%C3%A3o-por-construtor-8cd442884adc) 

```java
// imports

@Service
public class AlunoService {
	private AlunoRepository alunoRepository;
	
	public AlunoService(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}
	
}
```

## [Tratamento de erros](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)

Para tratar erros de forma global no Spring Boot utilizamos um Controller Advice, ele funciona centralizando a logica de tratamento de erros em uma única classe, essa classe em questão deve estar anotada com `@ControllerAdvice` ou `@RestControllerAdvice` sendo que utilizaremos a segunda anotação, pois ela facilita o tratamento de exceções embutindo o `@ControllerAdvice` com o `@ResponseBody` para ler mais, clique [aqui](https://docs.spring.io/spring-framework/reference/web/webflux/controller/ann-methods/responsebody.html).

Além disso devemos definir quais exceções e como tratá-las, para isso utilizamos ExceptionHandlers, que nada mais são do que métodos anotados com `@ExceptionHandler(Exception.class)` esse ExceptionHandler deve receber como argumento a classe da exceção que será tratada dentro daquele método, e a exceção lançada é passada como argumento de forma automática para o método que irá tratá-la, se quiséssemos tratar uma exceção NotFoundException poderíamos fazer da seguinte forma:

```java
// imports
@RestControllerAdvice
public class Handler {

    @ExceptionHandler(NotFoundException.class)
    public String notFoundExceptionHandler(NotFoundException e) {
        return "Ocorreu uma NotFoundException: " + e.getMessage();
    }

}
```

Desta forma, quando ocorrer uma exceção desse tipo, uma resposta contendo uma String  personalizada será retornada pela requisição, vale ressaltar que a resposta não se limita a tipos primitivos, também é possível retornar objetos personalizados. 

Outra prática comum é modificar o código HTTP da resposta, para isso podemos utilizar o [ResponseEntity](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/responseentity.html) do Spring da seguinte forma:

```java
// imports
@RestControllerAdvice
public class Handler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(NotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // requisição irá retornar com código 404
                .body("Ocorreu uma NotFoundException: " + e.getMessage());
    }

}
```

## Checklist

### [Model](https://github.com/GerdanyJr/weekIt/pull/1)

- [ ]  [Criar entidade Curso](https://github.com/GerdanyJr/weekIt/pull/1/commits/fc2cc8040720b032f935409cce8f508e12efd393)
- [ ]  [Criar entidade Aluno](https://github.com/GerdanyJr/weekIt/pull/1/commits/46307fb22200cb3ba7b89d673a13fb0a03d29e74)
- [ ]  [Criar entidade Participação](https://github.com/GerdanyJr/weekIt/pull/1/commits/2377dc7bcac955feb6442db02e902d607c0aa4e1)
- [ ]  [Criar relacionamento entre Aluno e Participação](https://github.com/GerdanyJr/weekIt/pull/1/commits/9b3c3a1467957d222d5f5c2c3d8027cb5f1a8de6)
- [ ]  [Criar relacionamento entre Curso e Participação](https://github.com/GerdanyJr/weekIt/pull/1/commits/751739d0cc79a26ce34d83552c4ae3f6984c9de1)

### [CRUD de Aluno](https://github.com/GerdanyJr/weekIt/pull/8)

- [ ]  [POST alunos/](https://github.com/GerdanyJr/weekIt/pull/2/commits) → Endpoint para cadastro de alunos
- [ ]  [GET alunos/](https://github.com/GerdanyJr/weekIt/pull/3/commits) → Endpoint para obter todos os alunos
- [ ]  [GET alunos/{id}](https://github.com/GerdanyJr/weekIt/pull/4/commits) → Endpoint para obter um aluno por id
- [ ]  [GET alunos/{nMatricula}](https://github.com/GerdanyJr/weekIt/pull/5/commits) → Endpoint para obter um aluno por numero de matrícula
- [ ]  [PATCH alunos/{id}](https://github.com/GerdanyJr/weekIt/pull/6/commits) → Endpoint para atualizar um aluno
- [ ]  [DELETE alunos/{id}](https://github.com/GerdanyJr/weekIt/pull/7/commits) → Endpoint para deletar um aluno

### [Tratamento de erros](https://github.com/GerdanyJr/weekIt/pull/12/commits)

- [ ]  [Criar resposta padronizada para erros](https://github.com/GerdanyJr/weekIt/commit/0fa2b6128ab3bc25743c69f92a73f34abeac2158)
- [ ]  [Implementar validação para evitar a criação duplicada de registros de alunos](https://github.com/GerdanyJr/weekIt/pull/9/commits)
- [ ]  [Implementar validação para evitar a duplicação na atualização de alunos](https://github.com/GerdanyJr/weekIt/commit/b2df743320cf51588760ccea226c9a3a7bdb5b38)
- [ ]  [Implementar lançamento de exceção para alunos não encontrados](https://github.com/GerdanyJr/weekIt/pull/10/commits)
- [ ]  [Implementar tratamento para exceção de validação (Bean Validation)](https://github.com/GerdanyJr/weekIt/pull/11/commits)

### [CRUD de Minicurso](https://github.com/GerdanyJr/weekIt/pull/18/commits)

- [ ]  [POST minicurso/](https://github.com/GerdanyJr/weekIt/pull/13/commits) → Endpoint para cadastrar um minicurso
- [ ]  [GET minicurso/](https://github.com/GerdanyJr/weekIt/pull/14/commits) → Endpoint para obter todos os minicursos
- [ ]  [GET minicurso/{id}](https://github.com/GerdanyJr/weekIt/pull/15/commits) → Endpoint para obter minicurso por id
- [ ]  [GET minicurso?search=””](https://github.com/GerdanyJr/weekIt/pull/16/commits) → Endpoint para buscar minicurso por nome
- [ ]  [PATCH minicursos/{id}](https://github.com/GerdanyJr/weekIt/pull/17/commits) → Endpoint para atualizar minicurso
- [ ]  [DELETE minicursos/{id}](https://github.com/GerdanyJr/weekIt/commit/719907b87ae53dca295b75d35b84f3788396d04d) → Endpoint para deletar minicurso

### CRUD de Participações

- [ ]  [POST participacoes/](https://github.com/GerdanyJr/weekIt/pull/19) → Endpoint para cadastrar uma participação
- [ ]  [GET participacoes/](https://github.com/GerdanyJr/weekIt/pull/20/commits) → Endpoint para obter todas as participações
- [ ]  [GET participacoes/{id}](https://github.com/GerdanyJr/weekIt/pull/21) → Endpoint para obter uma participação por id
- [ ]  [GET participacoes/{aluno/id}](https://github.com/GerdanyJr/weekIt/pull/22) → Endpoint para obter todas as participações por aluno
- [ ]  [GET participacoes/{minicurso/id}](https://github.com/GerdanyJr/weekIt/pull/23) → Endpoint para obter todas as participações por minicurso
- [ ]  PATCH participacoes/{id} → Endpoint para atualizar uma participação
- [ ]  DELETE participacoes/{id} → Endpoint para deletar uma participação
