const http = require("http");
const app = require("express")();
const { buildGraphbackAPI } = require("graphback");
const FruitDataProvider = require('./fruit-provider');
const { ApolloServer } = require("apollo-server-express");

const model = `
  """
  @model 
  """
  type Fruit {
    """
    @id 
    """
    _id: GraphbackObjectID!
    name: String
    description: String
  }

  scalar GraphbackObjectID
`;

// BootQuarkus
const Quarkus = Java.type('io.quarkus.runner.ApplicationImpl');
new Quarkus().start([])

// Use the dataProvider in buildGraphbackAPI
const { typeDefs, resolvers, contextCreator } = buildGraphbackAPI(model, {
  dataProviderCreator: () => new FruitDataProvider(),
});

const apolloServer = new ApolloServer({
  typeDefs,
  resolvers,
  context: contextCreator,
});

apolloServer.applyMiddleware({ app });

const httpServer = http.createServer(app);
apolloServer.installSubscriptionHandlers(httpServer);

httpServer.listen({ port: 4000 }, () => {
  console.log(`🚀  Server ready at http://localhost:4000/graphql`);
});
