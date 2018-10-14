from mpi4py import MPI
from operator import itemgetter
import time

def replace_First_Word(string_Archive, list_Words_Definitions):
	try:
		for word_Definition in list_Words_Definitions:
			string_Archive = string_Archive.replace(word_Definition[0] , word_Definition[1] , 1)		
		return string_Archive

	except Exception as e:
		return '\nError: ', e, '\n'
		raise

def save_archive(string_Archive, name_Archive):
	archive = open(name_Archive, 'w')
	archive.write(string_Archive)
	archive.close()

def read_all_archive(name_Archive):
	archive = open(name_Archive, 'r')
	string_Archive = archive.read()
	archive.close()
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

	try:
		comm = MPI.COMM_WORLD
		size = comm.Get_size()
		my_id = comm.Get_rank()
		name = MPI.Get_processor_name()
		next_process = (my_id + 1) % size;
		from_process = (my_id + size - 1) % size;
		tag_process = 100
		name_Archive = "libro_medicina.txt"
		name_Definitions = "palabras_libro_medicina.txt"
		data = []
		list_aux = []

		try:
			if my_id == 0:
				starting_point = time.time()
				string_Archive = read_all_archive(name_Archive)
				data.insert(0,[string_Archive]) #libro
				data.extend(return_Words_Definitions(read_lines_archive(name_Definitions), size))
				time = time.time () - starting_point
				print '\nProcess', my_id,' time: ', time
				print '\nProcess', my_id,' sending to Process', next_process
				comm.send(data, dest = next_process, tag = tag_process)
			else:
				print '\nProcess', my_id,' changing words: '
				data = comm.recv(source = from_process)
				new_Data = replace_First_Word(data[0][0], data[my_id])
				data[0] = [new_Data]
				time = time.time () - starting_point
				print '\nProcess', my_id,' ended, time: ', time
				print '\nProcess', my_id,' sending to Process', next_process
				comm.send(data, dest = next_process, tag = tag_process)			

			if my_id == 0:
				data = comm.recv(source = from_process)
				new_Data = data[0][0]
				save_archive(new_Data, name_Archive)
				time = time.time () - starting_point
				print '\nProcess', my_id,'ended, time: ', time

		except Exception as e:
			print '\nError: ', e, '\n'
			raise

	except Exception as e:
			print '\nError: ', e, '\n'
			raise