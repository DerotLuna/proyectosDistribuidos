from operator import itemgetter

def return_Amount_Words(name_Archive, list_Words):
	archive = open(name_Archive, "r")
	string_Archive = archive.read()
	hash_Words = {}
	#list_Words = []
	#list_Words = return_Words(name_Definitions)

	for word in list_Words:
		print word
		amount = string_Archive.count(word[0].lower()) + string_Archive.count(word[0].capitalize()) + string_Archive.count(word[0].upper())
		hash_Words[word[0].capitalize()] = [amount]

	archive.close()
	return sorted(hash_Words.items(), key = itemgetter(0)) #devuelve el hash ordenado por clave

def return_Words(name_Archive):
	archive = open(name_Archive, "r")
	line_Archive = archive.readlines()
	list_Words = []

	for line in line_Archive:
		part_line = line.split(" ")
		list_Words.append(part_line[0])

	archive.close()
	return list_Words

def return_Words_Definitions(name_Archive, size):
	archive = open(name_Archive, "r")
	line_Archive = archive.readlines()
	list_aux = []
	list_Words_Definitions = []
	counter = 1

	for line in line_Archive: 
		amount = len(line_Archive)/(size-1) #cantidad de palabras por nodo
		rest_amount = len(line_Archive)%(size-1)

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

	archive.close()
	return list_Words_Definitions



if __name__ == "__main__":
	
	name_Book = "libro_medicina.txt"
	name_Definitions = "palabras_libro_medicina.txt"
	size = 11

	hash_Words = []
	list_Words = []

	list_Words = return_Words_Definitions(name_Definitions, size)
	#list_Words2 = return_Words(name_Definitions2)

	hash_Words = return_Amount_Words(name_Book, list_Words)

	#hash_Words2 = return_Amount_Words(name_Book, list_Words2)

	#hash_Words = hash_Words + hash_Words2
	hash_Words = sorted(hash_Words, key = itemgetter(0))

	print hash_Words3