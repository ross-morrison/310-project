# 310-project
The project we have created is a chatbot, where the user of the program can input a command or question into the program and get a response from the program. The user will chat with a Dog about topics such as cats, squirrels and balls. The user will take on the role of themselves, will interact with an agent who will take on the role of a dog.
# How to compile
- Import project into eclipse
- Add JUnit and json-simple to build path
- Add Stanford POS tagger to build path from https://nlp.stanford.edu/software/tagger.shtml#Download 3.9.2 english version used
- Compile using Eclipse Run
- Use console to talk with the dog


# Added Features

-POS Tagging
      -Implemented for the way of entering the name to determine whether a singular pronoun is used
-GUI
      -The GUI feature did not necessarily improve our agent’s conversational skills, but it added an aesthetically pleasant medium for the user to interact with, simplifying the input and the output of the system.  
      
-SPELLCHECKER

     - The spellchecker feature is working everytime a user inputs a diolog into the gui. The spellchecker takes the user's input and 
     attempts to find the closest matching word from a dictionary. It implements Levenshtein distance to check how simular (how many
     different letters between) two words to attempt to pick the closest word to what the user typed. There if the input is too foriegn to
     any words in the dictionary (say 3 letters different from any word in the dictionary) the spellchecker will not change the word. This
     affects our conversation by reducing the amount of misscommunication between the user and chatbot.
     
-Ability to talk to other chatbots

     -Ability to talk to another agent: This feature allows our agent to talk to another agent via sockets. Instead of the user conversing with the dogbot, the dogbot can also converse with an agent from another cosc 310 team.

      
      
      
      
# Feature examples

-POS Tagging
      -type "My name is broken" first, followed by either another input or "My name is Alan"
      
-GUI
      -Will see visual feature upon running
      
-SPELLCHECKER

      -User inputs "I am very good at spelllng" the spellchecker will change the input automatically to ""I am very good at spelling"
      
-Ability to talk to other chatbots

      -OtherBot: "I like all the potatoes!"
      -OurBot:" Oh! A ball! Oh, boy! Oh, boy! A ball!"
      -OtherBot: "What do you need potatoes for?
      -OurBot: "I did not understand that"
      

# Authors
Eric Shanks,
Brandon Gaucher,
Ben Fitzharris,
Ross Morrison 
