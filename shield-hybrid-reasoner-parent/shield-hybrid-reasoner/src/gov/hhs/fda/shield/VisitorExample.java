package gov.hhs.fda.shield;


public class VisitorExample {

	public VisitorExample() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		VisitorExample e = new VisitorExample();
		Fruit[] fruits = new Fruit[] { e.new Apple("Green", 5), 
									   e.new Orange(10), 
									   e.new GiantMandarinOrange(8) };
		CalorieFruitVisitor calorieVisitor = e.new CalorieFruitVisitor();
		TalkFruitVisitor talkVisitor = e.new TalkFruitVisitor();
		for (Fruit fruit : fruits)
			  System.out.println(
					  fruit.getName()
				      + " has color " + fruit.getColor()
				      + ", has calories " + fruit.acceptVisitor(calorieVisitor)
				      + ", and likes to say, \"" + fruit.acceptVisitor(talkVisitor) + "\"");

	}
	
	interface Fruit {
		String getName();
		String getColor();
		Integer getWeight();
		<R> R acceptVisitor(FruitVisitor<R> visitor);
	}
	
	class Apple implements Fruit{
		String name = "Apple";
		String color;
		int weight;
		
		Apple(String color, int weight) {
			this.color = color;
			this.weight = weight;
			}

	
		public String getName() {
			return this.name;
		}

		public String getColor() {
			return this.color;
		}
		
		public Integer getWeight() {
			return this.weight;
		}
		
		public <R> R acceptVisitor(FruitVisitor<R> visitor) {
			return visitor.visit(this);
		}
	}
	
	class Orange implements Fruit {
		String name = "Orange";
		String color = "orange";
		int weight;
		
		Orange(int weight) {
			this.weight = weight;
			}

	
		public String getName() {
			return this.name;
		}

		public String getColor() {
			return this.color;
		}
		
		public Integer getWeight() {
			return this.weight;
		}
		
		public <R> R acceptVisitor(FruitVisitor<R> visitor) {
			return visitor.visit(this);
		}
	}
	
	class MandarinOrange extends Orange {
		String color = "orange";
		int weight;
		
		MandarinOrange(int weight) {
			super(weight);
			this.name = "MandarinOrange";
			}

		public String getName() {
			return this.name;
		}

		public String getColor() {
			return this.color;
		}
		
		public Integer getWeight() {
			return this.weight;
		}
		
		public <R> R acceptVisitor(FruitVisitor<R> visitor) {
			return visitor.visit(this);
		}
	}
	
	class GiantMandarinOrange extends MandarinOrange {
		String color = "orange";
		int weight;
		
		GiantMandarinOrange(int weight) {
			super(weight);
			this.name = "Giant Mandarin Orange";
			}

		public String getName() {
			return this.name;
		}

		public String getColor() {
			return this.color;
		}
		
		public Integer getWeight() {
			return this.weight;
		}
		
		public <R> R acceptVisitor(FruitVisitor<R> visitor) {
			return visitor.visit(this);
		}
	}



	interface FruitVisitor<R> {
		public R visit(Apple apple);
		public R visit(Orange orange);
		public R visit(MandarinOrange mandarinOrange);
		public R visit(GiantMandarinOrange giantMandarinOrange);
	}
	
	class CalorieFruitVisitor implements FruitVisitor<Integer> {
		
		public Integer visit(Apple apple) {
			return apple.getWeight() * 30;
		};
		public Integer visit(Orange orange){
			return orange.getWeight() * 50;
		}
		public Integer visit(MandarinOrange mandarinOrange){
			return mandarinOrange.getWeight() * 40;
		}
		public Integer visit(GiantMandarinOrange mandarinOrange){
			return mandarinOrange.getWeight() * 40;
		}

	}
	
	class TalkFruitVisitor implements FruitVisitor<String> {
		public String visit(Apple apple) {
			return "I'm an Apple!";
		};
		public String visit(Orange orange){
			return "I'm an Orange!";
		}
		public String visit(MandarinOrange mandarinOrange){
			return "I'm a Mandarin Orange!";
		}
		public String visit(GiantMandarinOrange mandarinOrange){
			return "I'm a Giant Mandarin Orange!";
		}
	}

	
}


