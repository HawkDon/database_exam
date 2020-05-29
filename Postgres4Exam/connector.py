import psycopg2
def connect2DB():
    return psycopg2.connect(    user = "postgres",
                                password = "password",
                                host = "postgres",   #docker
                                port = "5432",       #docker
                                #host = "localhost", #Localhost
                                #port = 27018,       #Localhost
                                database = "postgres")
