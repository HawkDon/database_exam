FROM node:10
WORKDIR /app/
COPY package.json package.json
RUN npm install
COPY . .
RUN chmod +x sleep.sh
EXPOSE 3000
CMD ["./sleep.sh"]