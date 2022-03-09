package ca.utoronto.cs.message;

public class BannerResponse extends Response {
	/**
	 *
	 * @return a BANNER, type of MessageType
	 */
	@Override
	protected MessageType getType() {
		return MessageType.BANNER;
	}

	public String message;
}
