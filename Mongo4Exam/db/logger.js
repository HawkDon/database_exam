const { createLogger, transports, format } = require("winston");
require("winston-mongodb").MongoDB;

const logger = createLogger({
  transports: [
    new transports.MongoDB({
      db: "mongodb://admin:password@mongodb/coursera", // localhost:27017 on local
      options: { useUnifiedTopology: true },
      collection: "courses_log",
      format: format.combine(format.timestamp(), format.json()),
    }),
  ],
});

module.exports = logger;
