from mpi4py import MPI
import time
from operator import itemgetter
import os

def return_Amount_Words(string_Archive, list_Words):
	""" Metodo que se encarga contar un serie de palabras en un determinado texto
	Ademas, regresa las palabras contadas de manera ordenada """
	hash_Words = {} #inicializo el hash para almacenar key:palabra y value:cantidad de veces que se repite el texto

	for word in list_Words:	#ciclo encargado de contar palabra por palabra en el texto. Las cuenta de 3 formas las palabras: Todas en mayuscula, todas en minusculas y la primera en mayuscula.
		amount = string_Archive.count(word[0].lower()) + string_Archive.count(word[0].capitalize()) + string_Archive.count(word[0].upper())
		hash_Words[word[0].capitalize()] = amount #almaceno palabra con su cantidad en el hash

	return sorted(hash_Words.items(), key = itemgetter(0)) #devuelve el hash ordenado por key:palabra

def save_archive(string_Archive, name_Archive):
	archive = open(name_Archive, 'w')
	archive.write(string_Archive)
	archive.close()

def read_all_archive(name_Archive):
	#Metodo que se encarga de leer un determinado archivo y devolverlo completo como un string
	os.chdir(os.environ['LOCAL_HOME']) #me muevo a donde esta el libro, que seria el local_home/rdluna.15 de cada nodo
	archive = open(name_Archive, 'r')
	string_Archive = archive.read()
	archive.close()
	os.chdir(os.environ['HOME'])
	return string_Archive

def read_lines_archive(name_Archive):
	#Metodo que se encarga de leer un determinado archivo y devolverlo en una lista que contiene una linea por item
	archive = open(name_Archive, "r")
	lines_Archive = archive.readlines()
	archive.close()
	return lines_Archive

def return_Words_Definitions(lines_Archive, size):
	""" Metodo que se encarga de picar lineas con el formato: palabra "definicion" 
	y lo guarda en una lista con tuplas [(palabra, definicion),(palabra, definicion),(palabra, definicion), etc]
	"""
	list_aux = []
	list_Words_Definitions = []
	list_Words_Definitions.extend([None])
	counter = 1

	for line in lines_Archive: 
		amount = len(lines_Archive)/(size-1) #cantidad de palabras por nodo
		rest_amount = len(lines_Archive)%(size-1)

		word_line = line.split(" ") #Se obtiene la palabra
		definition_line = line.split("\"") #Se obtiene la definicion
		wd_Tuple = (word_line[0],definition_line[1]) #Uno palabras con definicion
		list_aux.append(wd_Tuple) #agrego palabra con definicion a la lista de retorno

		if ((counter != size-1) and (len(list_aux) == amount)):
			list_Words_Definitions.extend([list_aux])
			list_aux = []
			counter += 1
		elif ((counter == size-1) and (len(list_aux) == amount + rest_amount)):
			list_Words_Definitions.extend([list_aux])

	return list_Words_Definitions

if __name__ == "__main__":

	comm = MPI.COMM_WORLD
	size = comm.Get_size()
	my_id = comm.Get_rank()
	#name = MPI.Get_processor_name()
	all_data = []
	counter_recv = 0

	name_Book = "libro_medicina.txt"
	name_Definitions = "palabras_libro_medicina.txt"

	if my_id == 0:
	   data = return_Words_Definitions(read_lines_archive(name_Definitions), size)
	   #print '\n \n We will be scattering: \n \n',data, ' \n \n'
	else:
	  data = None
	

	data = comm.scatter(data, root=0)
	starting_point = time.time()

	if my_id != 0: 
		data = return_Amount_Words(read_all_archive(name_Book),data)

	time = time.time () - starting_point
	print '\nProcess', my_id,' time: ', time

	data = comm.gather(data,root=0)
	
	if my_id == 0:
   		counter = 0

   		for node_data in data:
   			if counter != 0:
   				print ' \nProcess', counter, ' data : \n', node_data
   				all_data.extend(node_data)
   			counter += 1
 
   		all_data = sorted(all_data)
   		print '\nMaster data: \n \n', all_data, ' \n'