FROM python:3
WORKDIR /app/
COPY . .
RUN chmod +x sleep.sh
RUN pip3 install simplejson psycopg2-binary Flask flask_restful
EXPOSE 5000
CMD [ "./sleep.sh" ]