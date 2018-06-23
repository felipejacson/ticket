# ticket

This is the microservice to provide the endpoints to:

 - (GET)  List all tickets: /tickets
 - (GET)  List a especific ticket: /tickets/{id}
 - (GET)  List a especific ticket by city: /tickets/cidade/{id}
 - (POST) Insert a new ticket: /tickets/
      Params:
        - nome: Title of the ticket
        - descricao: Description of the ticket
        - data: Data of validation of the ticket
        - cidadeId: City of the ticket
        - fornecedorId: Responsable for the ticket
 - (PUT) Update a new ticket: /tickets/{id}
      Params:
        - nome: Title of the ticket
        - descricao: Description of the ticket
        - data: Data of validation of the ticket
        - cidadeId: City of the ticket
        - fornecedorId: Responsable for the ticket
  
