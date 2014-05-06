package simulator;

import java.util.Scanner;

import simulator.TxRxEvent.TxRxEventType;

public class TxRxSystem {

    // Parametros da simulação
	static int MAX_DATA  = 1;
	
	static int DATA_SIZE = 18;
	static double INTERVAL = 1.0;
	
	static double RITMO_BINARIO = 100000.0;
	static double DISTANCIA     = 1000.0; //metros 5000000
	
	static double Peb = 0.001;
	static double DSum = 0.0;
	
	// Contadores Estatisticos	
    static double delayQ   = 0.0;
	static double delayQtx = 0.0;
	static double delaySys = 0.0;
	
	static boolean demonstracao = false;
	static boolean simulacao = false;
	
	public static void main(String[] args) {
		Simulator.setLogLevel(Simulator.LogLevel.FULLDEBUG);	

		Simulator.setDataFile("C:\\Users\\Filipe\\Desktop\\data.txt");
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Demonstra�ao ou Simula�ao?(D / S): ");
		String ds = scanner.nextLine();
		if(ds.equals("D")) {
			demonstracao = true;
			System.out.print("Tamanho das tramas(m - bit)?: ");
			DATA_SIZE = scanner.nextInt();
			System.out.print("Intervalo entre Tramas(s- seg)?: ");
			INTERVAL = scanner.nextInt();
			System.out.print("Ritmo Bin�rio(Rb - bit/s)?: ");
			RITMO_BINARIO = scanner.nextDouble();
			System.out.print("Distancia entre Emissor e Receptor(d - metros)?: ");
			DISTANCIA = scanner.nextDouble();
			
		}
		else if(ds.equals("S")) {
			simulacao = true;
			System.out.print("Experiencia (1 / 2)?: ");
			String experiencia = scanner.nextLine();
			if(experiencia.equals("1")) {
				System.out.print("Probabilidade de erro de bit?: ");
				Peb = scanner.nextDouble();
				System.out.print("Tamanho das tramas(m - bit)?: ");
				DATA_SIZE = scanner.nextInt();
				RITMO_BINARIO = 10000000.0;
				MAX_DATA = 10000;//10000
			}
			else if(experiencia.equals("2")) {
				System.out.print("Tamanho das tramas(m - bit)?: ");
				DATA_SIZE = scanner.nextInt();
				System.out.print("Distancia entre Emissor e Receptor(d - metros)?: ");
				DISTANCIA = scanner.nextInt();
				Peb = 0.001;
				RITMO_BINARIO = 10000000.0;
				MAX_DATA = 10000;//10000
			}
			else {
				System.out.println("O programa vai terminar!");
				System.exit(0);
			}
		}
		else {
			System.out.println("O programa vai terminar!");
			System.exit(0);
		}
		
		String s="#Simulation Results \n";
		s=s+"#Ntramas \t"+MAX_DATA+"\n";
		s=s+"#L \t\t"+DATA_SIZE+"\n";
		s=s+"#TaxaChegadas \t"+INTERVAL+"\n";
		s=s+"#Rb \t\t"+RITMO_BINARIO+"\n";
		s=s+"#d \t\t"+DISTANCIA+"\n";		
		Simulator.data(s);
		
		/* Variáveis de estado - participantes no sistema */
		Source     source   = new Source(MAX_DATA, DATA_SIZE, INTERVAL);
		Receiver   receiver = new Receiver();
		Transmiter transmiter    = new Transmiter(RITMO_BINARIO, DISTANCIA);
		
		/* Evento que arranca a simulação - abertura do serviço no instante 0.0 */
		TxRxEvent seed = new TxRxEvent(0.0, TxRxEventType.Generate_DATA, null);
		Simulator.addEvent(seed);

		/* Evento actual - o que se está a processar em cada ciclo */
		TxRxEvent current = (TxRxEvent)Simulator.nextEvent();

		/* Ciclo central da simulação */
		while (current!=null) {
			switch ((TxRxEvent.TxRxEventType)(current.type())) {
			    // Processamento de cada um dos tipos de acontecimentos
				case Generate_DATA:
					source.generateData(current);
					break;
					
				case Arrival_DATA:
					transmiter.arrivalData((Data)current.data());
					break;
					
				case StopTX:
					transmiter.stopTx((Data)current.data());
					break;
					
				case StartRX:
					receiver.startRX((Data)(current.data()));
					break;
				
				case StopRX:
					receiver.stopRX((Data)(current.data()));
					break;
				case TIMEOUT:
					transmiter.timeout((Data)(current.data()));
					break;
				case ACK:
					Simulator.removeEventByTemplate(new TxRxEvent(0.0,  TxRxEventType.TIMEOUT,(Data) (current.data())));
					transmiter.acknowledge((Data)(current.data()));
					break;
			}					
			/* Retira da lista o próximo acontecimento */
			current = (TxRxEvent) Simulator.nextEvent();	
		}
		Simulator.info("FIM.");
		
		// Impressão dos resultados da simulação.
		Simulator.info("STATS.");
		s = "Avg. delayQ = "+(delayQ/(double)MAX_DATA)+"\n";
		s = s+"Avg. delayQTx = "+(delayQtx/(double)MAX_DATA)+"\n";
		s = s+"Avg. delaySys = "+(delaySys/(double)MAX_DATA)+"\n";
		s = s+"Numero medio de tramas na fila, Nq = "+(delayQ/Simulator.getClock())+"\n";
		s = s+"Numero medio de tramas na fila ou a transmitir, Nqtx = "+(delayQtx/Simulator.getClock())+"\n";
		s = s+"Numero medio de tramas no sistema total, Nsistema = "+(delaySys/Simulator.getClock())+"\n";
		s = s+"Atraso m�dio de tansfer�ncia por trama, D = "+ (DSum/MAX_DATA) +"\n";
		s = s+"Taxa de Utiliza�ao do meio, U = "+ ((MAX_DATA*(DATA_SIZE/RITMO_BINARIO))/Simulator.getClock()) +"\n";
		Simulator.info(s);
	}
		
}
