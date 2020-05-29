import psycopg2
def connect2DB():
    return psycopg2.connect(    user = "postgres",
                                password = "password",
                                host = "postgres",
                                port = "5432",
                                database = "postgres")
