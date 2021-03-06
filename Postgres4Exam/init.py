import psycopg2
import csv
import sys

def createDBStructure():
    try:
        create_schema = '''
        CREATE SCHEMA IF NOT EXISTS coursesschema;
        '''

        create_table_courses_level_query = '''
        CREATE TABLE IF NOT EXISTS coursesschema.level (
            id serial PRIMARY KEY,
            title text UNIQUE NOT NULL,
            description text NOT NULL
        )
        '''
        create_table_courses_subject_query = '''
        CREATE TABLE IF NOT EXISTS coursesschema.subject (
            id serial PRIMARY KEY,
            title text UNIQUE NOT NULL,
            description text NOT NULL
        )
        '''

        create_table_courses_query = '''
        CREATE TABLE IF NOT EXISTS coursesschema.courses (
            id serial PRIMARY KEY,
            title text NOT NULL,
            url text UNIQUE NOT NULL,
            paid boolean NOT NULL DEFAULT false,
            price int NOT NULL,
            number_subscribers int NOT NULL,
            number_reviews int NOT NULL,
            number_of_lectures int NOT NULL,
            duration numeric NOT NULL,
            level int REFERENCES coursesschema.level(id)
        )
        '''

        create_table_coursesAndsubject_query = '''
		CREATE TABLE IF NOT EXISTS coursesschema.coursesandsubject (
            id serial PRIMARY KEY NOT NULL,
            course_id int REFERENCES coursesschema.courses(id) ON DELETE CASCADE,
            subject_id int REFERENCES coursesschema.subject(id) ON DELETE CASCADE
        )
        '''

        create_table_log_query = '''
		CREATE TABLE IF NOT EXISTS coursesschema.log (
            id serial PRIMARY KEY,
            logmsg varchar(999) NOT NULL
        )
        '''

        storedprod_insert_level = '''
        CREATE OR REPLACE PROCEDURE coursesschema.insert_level(text, text)
        LANGUAGE plpgsql
	    AS $$
        BEGIN
	        INSERT INTO coursesschema.level (title, description) VALUES ($1,$2);
        END;
	    $$;
        '''
        storedprod_insert_subject = '''
        CREATE OR REPLACE PROCEDURE coursesschema.insert_subject(text, text)
        LANGUAGE plpgsql
	    AS $$
        BEGIN
	        INSERT INTO coursesschema.subject (title, description) VALUES ($1,$2);
        END;
	    $$;
        '''
        storedprod_insert_course = '''
        CREATE OR REPLACE PROCEDURE coursesschema.insert_course(text, text, boolean, int, int, int, int, int, numeric) 
        LANGUAGE plpgsql
	    AS $$
        BEGIN
	        INSERT INTO coursesschema.courses 
            (title, url, paid, price, number_subscribers, number_reviews, number_of_lectures, level, duration)
            VALUES 
            ($1,$2,$3,$4,$5,$6,$7,$8,$9);
        END;
	    $$;
        '''
        storedprod_insert_courseWithSubject = '''
        CREATE OR REPLACE PROCEDURE coursesschema.insert_course_with_subject(int, int)
        LANGUAGE plpgsql
	    AS $$
        BEGIN
	        INSERT INTO coursesschema.coursesandsubject 
            (course_id, subject_id)
            VALUES 
            ($1,$2);
        END;
	    $$;
        '''
        storedprod_insert_log = '''
        CREATE OR REPLACE PROCEDURE coursesschema.insert_log(text)
        LANGUAGE plpgsql
	    AS $$
        BEGIN
	        INSERT INTO coursesschema.log 
            (logmsg)
            VALUES 
            ($1);
        END;
	    $$;
        '''
        storedprod_get_single_course='''
        CREATE OR REPLACE FUNCTION show_single_courses(cid int) 
        RETURNS TABLE (course_id INT,title TEXT,url TEXT,paid BOOLEAN,price INT,number_subscribers INT,number_reviews INT,number_of_lectures INT,	duration NUMERIC, level TEXT) 
        AS $func$       
        BEGIN
	        RETURN QUERY SELECT courses.id, courses.title, courses.url, courses.paid, courses.price, courses.number_subscribers,
						        courses.number_reviews, courses.number_of_lectures, courses.duration, level.title 
				FROM coursesschema.courses 
				INNER JOIN 
				coursesschema.level on coursesschema.level.id = courses.level
				where courses.id = cid LIMIT 1;  
				RETURN;
        END;
        $func$ LANGUAGE plpgsql;
        '''
        storedprod_put_single_course = '''
        CREATE OR REPLACE PROCEDURE coursesschema.update_course(int, text, text, boolean, int, int, int, int, numeric, int)
        LANGUAGE plpgsql
	    AS $$
        BEGIN
            UPDATE coursesschema.courses
            SET title = $2,  url = $3, paid = $4, price = $5, number_subscribers = $6, number_reviews = $7, number_of_lectures = $8, duration = $9, level = $10
            WHERE id = $1;
        END;
	    $$;
        '''
        storedprod_put_single_course = '''
        CREATE OR REPLACE PROCEDURE coursesschema.update_course(int, text, text, boolean, int, int, int, int, numeric, int)
        LANGUAGE plpgsql
	    AS $$
        BEGIN
            UPDATE coursesschema.courses
            SET title = $2,  url = $3, paid = $4, price = $5, number_subscribers = $6, number_reviews = $7, number_of_lectures = $8, duration = $9, level = $10
            WHERE id = $1;
        END;
	    $$;
        '''

        storedprod_delete_single_course = '''
        CREATE OR REPLACE PROCEDURE coursesschema.delete_course(int)
        LANGUAGE plpgsql
	    AS $$
        BEGIN
            DELETE FROM coursesschema.courses WHERE id = $1;
        END;
	    $$;
        '''

        connection = connect2DB()
        cursor = connection.cursor()
        cursor.execute(create_schema)
        connection.commit()
        cursor.execute(create_table_courses_level_query)
        connection.commit()
        cursor.execute(create_table_courses_subject_query)
        connection.commit()
        cursor.execute(create_table_courses_query)
        connection.commit()
        cursor.execute(create_table_coursesAndsubject_query)
        connection.commit()
        cursor.execute(create_table_log_query)
        connection.commit()
        cursor.execute(storedprod_insert_level)
        connection.commit()
        cursor.execute(storedprod_insert_subject)
        connection.commit()
        cursor.execute(storedprod_insert_course)
        connection.commit()
        cursor.execute(storedprod_insert_courseWithSubject)
        connection.commit()
        cursor.execute(storedprod_insert_log)
        connection.commit()
        cursor.execute(storedprod_get_single_course)
        connection.commit()
        cursor.execute(storedprod_put_single_course)
        connection.commit()
        cursor.execute(storedprod_delete_single_course)
        connection.commit() 
    except (psycopg2.Error) as error:
        connection.rollback()
        print (f"Couldn't open and populate db from file", error, file=sys.stdout)
    finally:
        cursor.close()
        connection.close()    

def populateDB():
    try:
        connection = connect2DB()
        cursor = connection.cursor()
        cursor.execute("SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'coursesschema';")
        result = cursor.fetchone()
        if(result is not None):
            return
        createDBStructure()
        levelArray = []
        subjectArray = []
        courseUrlArray = []
       
        with open('udemy_courses.csv', encoding='utf-8', newline='') as csvfile:
            reader = csv.reader(csvfile, delimiter=',', quotechar='"')
            next(reader)
            price = 50
            for row in reader:
                if(row[8] not in levelArray):
                    price += 50
                    print( levelArray.append(row[8]), file=sys.stdout )
                    INIT_insertCourseLevel(row[8])
                if(row[11] not in subjectArray):
                    subjectArray.append(row[11])
                    INIT_insertSubject(row[11])

        with open('udemy_courses.csv', encoding='utf-8', newline='') as csvfile:
            reader = csv.reader(csvfile, delimiter=',', quotechar='"')
            next(reader)
            price = 50
            for row in reader:
                if(row[11] == ""):
                    continue
                else:
                    courseUrlArray.append(row[2])
                    INIT_insertCourse(row,levelArray)
                    INIT_insertCoursesAndSubject(row,subjectArray,courseUrlArray)

    except (psycopg2.Error) as error:
        #connection.rollback()
        insertLog(error)
        print (f"failed to init DB: ", error, flush=True)



def INIT_insertCourseLevel(level):
    try:
        connection = connect2DB()
        cursor = connection.cursor()
        cursor.execute('CALL coursesschema.insert_level(%s,%s);', (level, "Generic description") )
        connection.commit()
    except (psycopg2.Error) as error:
        #Checking for duplicate unique keys
        if("23505" not in error.pgcode):
            print (f"ERROR: insert course level ", error, flush=True)
            connection.rollback()
        insertLog(error)
    finally:
        cursor.close()
        connection.close()


def INIT_insertSubject(subject):
    try:
        connection = connect2DB()
        cursor = connection.cursor()
        cursor.execute('CALL coursesschema.insert_subject(%s,%s)',(subject,"Some description about a subject"))
        connection.commit()
    except (psycopg2.Error) as error:
        #Checking for duplicate unique keys
        if("23505" not in error.pgcode):
            print (f"ERROR: insert subject  ",  error, flush=True)
            connection.rollback()
        insertLog(error)
    finally:
        cursor.close()
        connection.close()

def INIT_insertCourse(row,levelArr):
    try:
        index = levelArr.index(row[8])
        connection = connect2DB()
        cursor = connection.cursor()
        cursor.execute('CALL coursesschema.insert_course(%s,%s,%s,%s,%s,%s,%s,%s,%s)', [row[1], row[2], row[3], row[4], row[5], row[6], row[7], index+1, row[9] ] )
        connection.commit()
    except (psycopg2.Error) as error:
        #Checking for duplicate unique keys
        if("23505" not in error.pgcode):
            print (f"ERROR: insert course  ",  error, flush=True)
            connection.rollback()
        insertLog(error)
    finally:
        cursor.close()
        connection.close()

def INIT_insertCoursesAndSubject(row,subjectArr,courseUrlArr):
    try:
        indexSubject = subjectArr.index(row[11])
        indexCourseUrl = courseUrlArr.index(row[2])
        connection = connect2DB()
        cursor = connection.cursor()
        cursor.execute('CALL coursesschema.insert_course_with_subject(%s,%s)',[indexCourseUrl+1, indexSubject+1])
        connection.commit()
    except (psycopg2.Error) as error:
        #Checking for duplicate unique keys
        if("23505" not in error.pgcode):
            print (f"ERROR: insert course  ",  error, flush=True)
            connection.rollback()
        insertLog(error)
    finally:
        cursor.close()
        connection.close()

def insertLog(error):
    try:
        #record_to_insert = [error.pgerror]
        connection = connect2DB()
        cursor = connection.cursor()
        cursor.execute("CALL coursesschema.insert_log(%s)", [error.pgerror])
        connection.commit()
    except (psycopg2.Error) as error:
        print (f"LOG ERROR: ", error, file=sys.stderr)
        connection.rollback()
    finally:
        cursor.close()
        connection.close()

def connect2DB():
    return psycopg2.connect(    user = "postgres",
                                password = "password",
                                host = "postgres",
                                port = "5432",
                                database = "postgres")
