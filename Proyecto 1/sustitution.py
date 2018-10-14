
def replace_First_Word(name_Archive, list_Words_Definitions):
	archive = open(name_Archive, 'r')
	string_Archive = archive.read()

	for word_Definition in list_Words_Definitions:
		string_Archive = string_Archive.replace(word_Definition[0] , word_Definition[1] , 1)

	archive.close()
	archive = open(name_Archive, 'w')
	archive.write(string_Archive)
	archive.close()

def return_Words_Definitions(name_Archive):
	archive = open(name_Archive, "r")
	line_Archive = archive.readlines()
	list_Words_Definitions = []

	for line in line_Archive:
		word_line = line.split(" ") #Se obtiene la palabra
		definition_line = line.split("\"") #Se obtiene la definicion		
		wd_Tuple = (word_line[0],definition_line[1]) #Uno palabras con definicion
		list_Words_Definitions.append(wd_Tuple) #agrego palabra con definicion a la lista de retorno

	archive.close()
	return list_Words_Definitions

if __name__ == "__main__":

	book = "libro.txt"
	words = "Palabras_Grupo12.txt"
	list_Words_Definitions = return_Words_Definitions(words)
	replace_First_Word(book, list_Words_Definitions)
	