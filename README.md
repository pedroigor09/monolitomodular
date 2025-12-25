# 🏗️ Monólito Modular com Clean Architecture

## 📚 O QUE É ESTE PROJETO?

Este é um **sistema de pedidos (orders)** implementado seguindo os princípios de **Clean Architecture** (Arquitetura Limpa) e **Monólito Modular**.

### 🎯 Objetivo de Aprendizado

Este projeto foi criado para você **APRENDER NA PRÁTICA**:
- ✅ Clean Architecture (Arquitetura Limpa)
- ✅ Separação de Responsabilidades (SoC)
- ✅ Inversão de Dependência (DIP)
- ✅ Domain-Driven Design (DDD) básico
- ✅ Aggregate Roots e Entidades
- ✅ Use Cases
- ✅ Ports & Adapters (Hexagonal Architecture)

---

## 🧱 ARQUITETURA

### As 4 Camadas (de dentro para fora):

```
┌─────────────────────────────────────────────┐
│         PRESENTATION (Controllers)          │  ← HTTP/REST
├─────────────────────────────────────────────┤
│      APPLICATION (Use Cases + DTOs)         │  ← Orquestração
├─────────────────────────────────────────────┤
│      INFRASTRUCTURE (JPA + Configs)         │  ← Detalhes técnicos
├─────────────────────────────────────────────┤
│         DOMAIN (Entidades + Regras)         │  ← Coração da aplicação
└─────────────────────────────────────────────┘
```

### 📦 Estrutura de Pastas

```
src/main/java/com/example/monolitomodular/
│
├── domain/                         # ❤️ NÚCLEO - Regras de Negócio Puras
│   ├── order/
│   │   ├── Order.java             # Aggregate Root
│   │   ├── OrderItem.java         # Entity
│   │   ├── OrderStatus.java       # Value Object (Enum)
│   │   └── OrderRepository.java   # PORTA (interface)
│   │
│   ├── customer/
│   │   ├── Customer.java          # Entity
│   │   └── CustomerRepository.java # PORTA (interface)
│   │
│   └── shared/
│       └── DomainException.java   # Exception do domínio
│
├── application/                    # 🎯 Casos de Uso
│   ├── order/
│   │   ├── CreateOrderUseCase.java
│   │   ├── GetOrderUseCase.java
│   │   ├── ConfirmOrderUseCase.java
│   │   └── dto/
│   │       ├── CreateOrderRequest.java
│   │       ├── OrderResponse.java
│   │       └── OrderItemRequest.java
│   │
│   └── customer/
│       ├── CreateCustomerUseCase.java
│       ├── GetCustomerUseCase.java
│       └── dto/
│           ├── CreateCustomerRequest.java
│           └── CustomerResponse.java
│
├── infrastructure/                 # 🔧 Implementações Técnicas
│   ├── persistence/
│   │   ├── order/
│   │   │   ├── OrderEntity.java          # JPA Entity
│   │   │   ├── OrderItemEntity.java      # JPA Entity
│   │   │   ├── JpaOrderRepository.java   # Spring Data
│   │   │   └── OrderRepositoryImpl.java  # ADAPTER (implementa porta)
│   │   │
│   │   └── customer/
│   │       ├── CustomerEntity.java
│   │       ├── JpaCustomerRepository.java
│   │       └── CustomerRepositoryImpl.java
│   │
│   └── config/
│       └── BeanConfiguration.java  # Configuração Spring
│
└── presentation/                   # 🌐 Controllers REST
    ├── order/
    │   └── OrderController.java
    ├── customer/
    │   └── CustomerController.java
    └── shared/
        └── GlobalExceptionHandler.java
```

---

## 🎓 ENTENDENDO CADA CAMADA

### 1️⃣ DOMAIN (Coração da Aplicação)

**O QUE É?**
- Regras de negócio **PURAS**
- Não conhece banco de dados, frameworks, HTTP
- Apenas Java puro (POJOs)

**CONCEITOS:**

#### 🔹 Entidade (Entity)
```java
// Customer.java - TEM identidade própria (ID)
public class Customer {
    private Long id;  // ← IDENTIDADE
    private String name;
    private String email;
    
    // REGRAS DE NEGÓCIO dentro da entidade
    private void validate() {
        if (email inválido) throw new DomainException();
    }
}
```

#### 🔹 Aggregate Root
```java
// Order.java - RAIZ do agregado (controla OrderItems)
public class Order {
    private List<OrderItem> items;
    
    // ENCAPSULA operações no agregado
    public void addItem(OrderItem item) {
        if (status != PENDING) throw exception;
        items.add(item);
    }
}
```

#### 🔹 Value Object
```java
// OrderStatus.java - Valor imutável sem identidade
public enum OrderStatus {
    PENDING, CONFIRMED, SHIPPED
}
```

#### 🔹 Repository Interface (PORTA)
```java
// OrderRepository.java - INTERFACE no domínio
public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
}
// A IMPLEMENTAÇÃO fica na Infrastructure!
```

**POR QUE SEPARAR?**
- ✅ Domínio não depende de JPA, Spring, banco
- ✅ Pode testar regras sem frameworks
- ✅ Fácil trocar banco de dados

---

### 2️⃣ APPLICATION (Orquestração)

**O QUE É?**
- Casos de uso da aplicação
- Orquestra chamadas ao domínio
- Converte entre DTOs e entidades

**EXEMPLO:**
```java
// CreateOrderUseCase.java
public class CreateOrderUseCase {
    
    public OrderResponse execute(CreateOrderRequest request) {
        // 1. Valida cliente existe
        // 2. Cria Order (domínio faz validações)
        // 3. Adiciona items
        // 4. Salva no repository
        // 5. Converte para DTO
        return toResponse(savedOrder);
    }
}
```

**RESPONSABILIDADES:**
- ✅ Validações de aplicação (email único, etc)
- ✅ Orquestração de fluxo
- ✅ Conversão DTO ↔ Entidade
- ❌ NÃO contém regras de negócio complexas

**DTOs (Data Transfer Objects):**
```java
// CreateOrderRequest.java - Contrato de entrada
public record CreateOrderRequest(
    @NotNull Long customerId,
    @Valid List<OrderItemRequest> items
) {}
```

---

### 3️⃣ INFRASTRUCTURE (Detalhes Técnicos)

**O QUE É?**
- Implementações concretas de persistência
- Configurações de frameworks
- JPA, Spring Data, banco de dados

**CONCEITOS:**

#### 🔹 JPA Entity (diferente de Domain Entity!)
```java
// OrderEntity.java - Anotações JPA
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue
    private Long id;
    
    @OneToMany(cascade = ALL)
    private List<OrderItemEntity> items;
}
```

#### 🔹 Repository Implementation (ADAPTER)
```java
// OrderRepositoryImpl.java - Implementa PORTA do domínio
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    
    private final JpaOrderRepository jpaRepo;
    
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);  // Converte
        OrderEntity saved = jpaRepo.save(entity);
        return toDomain(saved);  // Converte de volta
    }
}
```

**POR QUE 2 ENTIDADES (Domain + JPA)?**
- ✅ Domínio fica limpo (sem anotações)
- ✅ Fácil trocar JPA por MongoDB, etc
- ✅ Separação de responsabilidades

**Bean Configuration:**
```java
// Cria instâncias dos Use Cases e injeta dependências
@Configuration
public class BeanConfiguration {
    @Bean
    public CreateOrderUseCase createOrderUseCase(
        OrderRepository orderRepo,
        CustomerRepository customerRepo
    ) {
        return new CreateOrderUseCase(orderRepo, customerRepo);
    }
}
```

---

### 4️⃣ PRESENTATION (Interface com o Mundo)

**O QUE É?**
- Controllers REST
- Recebe HTTP requests
- Chama Use Cases
- Retorna HTTP responses

**EXEMPLO:**
```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final CreateOrderUseCase createOrderUseCase;
    
    @PostMapping
    public ResponseEntity<OrderResponse> create(
        @Valid @RequestBody CreateOrderRequest request
    ) {
        OrderResponse response = createOrderUseCase.execute(request);
        return ResponseEntity.status(CREATED).body(response);
    }
}
```

**RESPONSABILIDADES:**
- ✅ Protocolo HTTP (GET, POST, status codes)
- ✅ Validação de entrada (@Valid)
- ✅ Conversão JSON ↔ DTO
- ❌ NÃO contém lógica de negócio!

---

## 🔄 FLUXO DE UMA REQUISIÇÃO

```
1. HTTP POST /api/orders
   ↓
2. OrderController recebe request
   ↓
3. @Valid valida o DTO
   ↓
4. Controller chama CreateOrderUseCase
   ↓
5. Use Case:
   - Valida cliente existe (via CustomerRepository)
   - Chama Order.create() (domínio)
   - Chama Order.addItem() para cada item (domínio)
   - Chama OrderRepository.save()
   ↓
6. OrderRepositoryImpl:
   - Converte Order → OrderEntity
   - Salva no banco (Spring Data)
   - Converte OrderEntity → Order
   ↓
7. Use Case converte Order → OrderResponse
   ↓
8. Controller retorna HTTP 201 Created + JSON
```

---

## 🚀 COMO RODAR O PROJETO

### 1. Pré-requisitos
- Java 17+
- Maven 3.8+

### 2. Rodar a aplicação
```bash
mvn spring-boot:run
```

### 3. Acessar
- **API REST**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:monolitodb`
  - Username: `sa`
  - Password: (vazio)

---

## 📝 TESTANDO A API

### 1. Criar Cliente
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "phone": "11987654321"
  }'
```

### 2. Listar Clientes
```bash
curl http://localhost:8080/api/customers
```

### 3. Criar Pedido
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {
        "productName": "Notebook",
        "quantity": 1,
        "unitPrice": 2500.00
      },
      {
        "productName": "Mouse",
        "quantity": 2,
        "unitPrice": 50.00
      }
    ]
  }'
```

### 4. Buscar Pedido
```bash
curl http://localhost:8080/api/orders/1
```

### 5. Confirmar Pedido
```bash
curl -X POST http://localhost:8080/api/orders/1/confirm
```

### 6. Listar Pedidos de um Cliente
```bash
curl http://localhost:8080/api/orders/customer/1
```

---

## 🎯 COMO APRENDER COM ESTE PROJETO

### 📖 Roteiro de Estudo

#### Dia 1: Domínio
1. Leia **domain/order/Order.java**
   - Veja os métodos de negócio: `addItem()`, `confirm()`, `cancel()`
   - Entenda as validações
2. Leia **domain/order/OrderItem.java**
   - Veja `calculateSubtotal()`
3. Leia **domain/order/OrderRepository.java**
   - É só uma **interface**! A implementação está na Infrastructure

**EXERCÍCIO:**
- Adicione uma regra: "Pedido só pode ser cancelado se total < R$ 1000"
- Adicione método `canBeCancelled()` na classe Order

#### Dia 2: Application
1. Leia **application/order/CreateOrderUseCase.java**
   - Veja como orquestra o fluxo
   - Veja as conversões DTO → Domain
2. Teste os endpoints com curl
3. Olhe os logs no console (SQL sendo executado)

**EXERCÍCIO:**
- Crie um novo Use Case: `CancelOrderUseCase`

#### Dia 3: Infrastructure
1. Leia **infrastructure/persistence/order/OrderEntity.java**
   - Compare com **domain/order/Order.java**
   - Veja as anotações JPA
2. Leia **OrderRepositoryImpl.java**
   - Veja os mappers `toDomain()` e `toEntity()`
3. Abra H2 Console e veja as tabelas

**EXERCÍCIO:**
- Adicione um novo campo "notes" (observações) no pedido
- Propague mudança em todas as camadas

#### Dia 4: Presentation
1. Leia **presentation/order/OrderController.java**
   - Veja como é simples (só chama Use Cases)
2. Leia **GlobalExceptionHandler.java**
   - Veja como trata erros de negócio

**EXERCÍCIO:**
- Adicione endpoint `DELETE /api/orders/{id}`
- Crie o Use Case correspondente

---

## 🧪 PRINCÍPIOS APLICADOS

### 1. Dependency Inversion Principle (DIP)
```java
// Domain define a interface (PORTA)
public interface OrderRepository { ... }

// Infrastructure implementa (ADAPTER)
public class OrderRepositoryImpl implements OrderRepository { ... }

// Application usa a interface, não a implementação!
public class CreateOrderUseCase {
    private final OrderRepository repository;  // ← Interface!
}
```

### 2. Single Responsibility Principle (SRP)
- **Domain**: Apenas regras de negócio
- **Application**: Apenas orquestração
- **Infrastructure**: Apenas persistência
- **Presentation**: Apenas HTTP

### 3. Separation of Concerns (SoC)
- JPA só na Infrastructure
- HTTP só na Presentation
- Regras de negócio só no Domain

---

## 🎓 CONCEITOS IMPORTANTES

### Aggregate Root
- **Order** é um Aggregate Root
- Controla o acesso aos **OrderItems**
- Você NÃO manipula OrderItem diretamente, só via Order

### Factory Methods
```java
// Construtor privado, força uso do factory
private Order() {}

// Factory method com validações
public static Order create(Long customerId) {
    Order order = new Order();
    order.validate();
    return order;
}
```

### Reconstitute Pattern
```java
// Usado pela Infrastructure ao buscar do banco
public static Order reconstitute(Long id, ...) {
    // Não valida (dados já validados no passado)
    return order;
}
```

---

## 💡 PRÓXIMOS PASSOS

### Nível Intermediário
1. Adicionar eventos de domínio (Domain Events)
2. Implementar testes unitários do domínio
3. Adicionar paginação nos listagens
4. Implementar autenticação/autorização

### Nível Avançado
1. Separar em módulos Maven (multi-module)
2. Adicionar CQRS (Command Query Responsibility Segregation)
3. Implementar Event Sourcing
4. Adicionar mensageria (RabbitMQ/Kafka)

---

## 📚 REFERÊNCIAS

- **Clean Architecture** - Robert C. Martin (Uncle Bob)
- **Domain-Driven Design** - Eric Evans
- **Implementing Domain-Driven Design** - Vaughn Vernon
- **Ports & Adapters (Hexagonal Architecture)** - Alistair Cockburn

---

## 🎯 RESUMO FINAL

**Por que Clean Architecture?**
- ✅ Domínio protegido de frameworks
- ✅ Fácil testar regras de negócio
- ✅ Fácil trocar tecnologias (banco, API, etc)
- ✅ Código organizado e manutenível

**O que você aprendeu?**
- ✅ Separação em camadas (Domain, Application, Infrastructure, Presentation)
- ✅ Inversão de dependência (Ports & Adapters)
- ✅ Aggregate Roots e Entities
- ✅ Use Cases
- ✅ DTOs e mappers
- ✅ Factory methods

**Como garantir que aprendeu?**
1. Tente explicar para alguém (ou para você mesmo!)
2. Faça os exercícios propostos
3. Crie uma nova feature do zero
4. Refatore alguma parte sem quebrar testes

---

## 🏆 DESAFIO FINAL

**Implemente um módulo de Pagamentos:**

1. **Domain**: Payment entity, PaymentStatus, PaymentRepository
2. **Application**: ProcessPaymentUseCase
3. **Infrastructure**: PaymentEntity, PaymentRepositoryImpl
4. **Presentation**: PaymentController

**Regras:**
- Pagamento vinculado a um Order
- Status: PENDING, APPROVED, REJECTED
- Ao aprovar pagamento, Order muda para CONFIRMED

Boa sorte! 🚀
