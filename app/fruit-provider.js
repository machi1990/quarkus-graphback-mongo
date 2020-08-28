const queryBuilder = require('./query-builder');

class FruitDataProvider {

  constructor() {
    this.Fruit = Java.type('me.machi.Fruit');
    const Arc = Java.type('io.quarkus.arc.Arc');
    this.fruitService = Arc.container().instance('fruit-service').get()
  }

  create(data) {
    const fruit = new this.Fruit(null, data.name, data.description);
    const addedFruit = this.fruitService.add(fruit);
    return JSON.parse(JSON.stringify(addedFruit));
  }

  update(data) {
    const { _id, ...rest } = data;
    return this.fruitService.update(data._id.toString(), JSON.stringify({
      $set: rest
    }));
  }

  delete(data) {
    return this.fruitService.delete(data._id.toString());
  }

  findOne(args) {
    const fruit = this.fruitService.findOne(args._id.toString());
    return JSON.parse(JSON.stringify(fruit));
  }

  count(filter) {
    const query = queryBuilder(filter, false);
    return this.fruitService.count(JSON.stringify(query));
  }

  findBy(filter) {
    const query = queryBuilder(filter, false);
    const fruits = this.fruitService.list(JSON.stringify(query));
    return JSON.parse(JSON.stringify(fruits));
  }

  batchRead(relationField, ids, filter) {
    const result = JSON.parse(JSON.stringify(this.findBy(filter)));
    return ids.map((objId) => {
      const objectsForId = [];
      for (const data of result) {
        if (data[relationField].toString() === objId.toString()) {
          objectsForId.push(data);
        }
      }

      return objectsForId;
    });
  }
}

module.exports = FruitDataProvider